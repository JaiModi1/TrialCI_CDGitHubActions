package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.apidef.AadhaarOTP;
import com.digitallending.userservice.apidef.AadhaarOTPCredential;
import com.digitallending.userservice.apidef.AadhaarPANLink;
import com.digitallending.userservice.apidef.PANInformation;
import com.digitallending.userservice.config.UserServiceConfigurationProperties;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.AadhaarPANLinkException;
import com.digitallending.userservice.exception.ExternalServiceException;
import com.digitallending.userservice.exception.PANInformationVerifyException;
import com.digitallending.userservice.exception.PreviousStepsNotDoneException;
import com.digitallending.userservice.exception.RegexMisMatchException;
import com.digitallending.userservice.exception.WrongAadhaarNoException;
import com.digitallending.userservice.exception.WrongOTPException;
import com.digitallending.userservice.model.dto.kyc.aadhaarpanlink.AadhaarPANInfoDTO;
import com.digitallending.userservice.model.dto.kyc.aadhaarpanlink.AadhaarPANLinkResponseDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.AadhaarDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.AadhaarOTPCredentialDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.AccessTokenDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.TransactionIDResponseDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.VerifyOTPReqeustDTO;
import com.digitallending.userservice.model.dto.kyc.mobileotp.VerifyMobileOTPDTO;
import com.digitallending.userservice.model.dto.kyc.paninformation.PANInformationRequestDTO;
import com.digitallending.userservice.model.dto.kyc.paninformation.PANInformationResponseDTO;
import com.digitallending.userservice.model.dto.kyc.paninformation.PANRequestDTO;
import com.digitallending.userservice.model.dto.kyc.paninformation.SourceOutput;
import com.digitallending.userservice.model.dto.transaction.VerifyTransactionRequestDTO;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.entity.UserKYCDetails;
import com.digitallending.userservice.service.def.KYCService;
import com.digitallending.userservice.service.def.RSAService;
import com.digitallending.userservice.service.def.TwilioService;
import com.digitallending.userservice.service.def.UserDetailsService;
import com.digitallending.userservice.service.def.UserKYCDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.UUID;

@Service
public class KYCServiceImpl implements KYCService {

    @Qualifier("retrofitPANAadhaarLink")
    @Autowired
    private Retrofit retrofitPanVerification;

    @Qualifier("retrofitPANInformation")
    @Autowired
    private Retrofit retrofitPanInformation;

    @Qualifier("aadhaarOTPCredential")
    @Autowired
    private Retrofit retrofitAadhaarOTPCredential;

    @Qualifier("aadhaarOTP")
    @Autowired
    private Retrofit retrofitAadhaarOTP;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserKYCDetailsService userKYCDetailsService;

    @Autowired
    private UserServiceConfigurationProperties userServiceConfigurationProperties;

    @Autowired
    private TwilioService twilioService;

    @Autowired
    private RSAService rsaService;

    private static final String aadhaarRegex = "^[2-9]{1}[0-9]{11}$";

    private static final String panRegex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

    @Override
    public void sendMobileOtp(String mobileNo, UUID userId) {
        UserOnBoardingStatus userStatus = userDetailsService.getUserStatus(userId);
        if (userStatus.equals(UserOnBoardingStatus.SIGN_UP)) {
            throw new PreviousStepsNotDoneException("Please First Verify Email Address");
        }
        twilioService.sendMobileOTP(mobileNo);

    }

    @Override
    public String sendMobileOtpLoanDisbursal(UUID userId) {
        UserDetails userDetails = userDetailsService.getUserDetails(userId);
        return twilioService.sendMobileOTP("+" + userDetails.getPhoneNo());
    }

    @Override
    public void verifyMobileOtpLoanDisbursal(VerifyTransactionRequestDTO request) {

        twilioService.verifyMobileOTPByVerificationSID(request.getTxnId(), request.getOtp().toString());
    }

    @Override
    @Transactional
    public void verifyMobileOtp(VerifyMobileOTPDTO verifyMobileOTPDTO, UUID userId) {

        twilioService.verifyMobileOTP(verifyMobileOTPDTO.getMobileNumber(), verifyMobileOTPDTO.getOtp());
        UserDetails userDetails = userDetailsService.getUserDetails(userId);
        userDetails.setPhoneNo(Long.valueOf(verifyMobileOTPDTO.getMobileNumber()));
        if (userDetails.getOnBoardingStatus().equals(UserOnBoardingStatus.VERIFY_EMAIL)) {
            userDetails.setOnBoardingStatus(UserOnBoardingStatus.VERIFY_PHONE);
        }
        userDetailsService.updateUserDetails(userDetails);
    }

    @Override
    public TransactionIDResponseDTO sendAadhaarOTP(AadhaarDTO aadhaarDTO, UUID userId) {

        String decodedAadhaarNo = rsaService.decodeMessage(aadhaarDTO.getAadhaar());

        if (!(decodedAadhaarNo.matches(aadhaarRegex))) {
            throw new RegexMisMatchException("Please Provide Valid Aadhaar Number");
        }

        UserOnBoardingStatus userStatus = userDetailsService.getUserStatus(userId);

        if (!userStatus.equals(UserOnBoardingStatus.VERIFY_PHONE)) {
            throw new PreviousStepsNotDoneException("Please First Verify Mobile No");
        }
        String accessToken = getAccessToken();

        UserKYCDetails userKYCDetails = UserKYCDetails.builder()
                .aadharNo(aadhaarDTO.getAadhaar())
                .userId(userId)
                .build();
        userKYCDetailsService.saveUserKYCDetailsByUserId(userKYCDetails);

        AadhaarOTP aadhaarOTP = retrofitAadhaarOTP.create(AadhaarOTP.class);
        aadhaarDTO.setAadhaar(decodedAadhaarNo);
        Response<TransactionIDResponseDTO> response;
        try {
            response = aadhaarOTP.generateOTP(aadhaarDTO, "Bearer " + accessToken).execute();
        } catch (IOException e) {
            throw new ExternalServiceException("Error occurred during aadhaar OTP generation");
        }
        if (response.code() != 200) {
            throw new ExternalServiceException("Error occurred during aadhaar OTP generation");
        }
        return response.body();

    }

    private String getAccessToken() {
        AadhaarOTPCredentialDTO aadhaarOTPCredentialDTO = new AadhaarOTPCredentialDTO(userServiceConfigurationProperties.getAadhaarClientId(), userServiceConfigurationProperties.getAadhaarClientSecret());

        AadhaarOTPCredential aadhaarOTPCredential = retrofitAadhaarOTPCredential.create(AadhaarOTPCredential.class);
        Response<AccessTokenDTO> response;
        String errorMessage = "Error occurred during getting aadhaar access Token for generate otp or verify otp";
        try {
            response = aadhaarOTPCredential.getaccessToken(aadhaarOTPCredentialDTO).execute();
        } catch (IOException e) {
            throw new ExternalServiceException(errorMessage);
        }
        if (response.code() != 200) {
            throw new ExternalServiceException(errorMessage);
        }
        AccessTokenDTO accessTokenDTO = response.body();
        if (accessTokenDTO == null) {
            throw new ExternalServiceException(errorMessage);
        }
        return accessTokenDTO.getAccessToken();

    }

    @Override
    @Transactional
    public void verifyAadhaarOTP(VerifyOTPReqeustDTO verifyOTPReqeustDTO, UUID userId) {


        String decodedRequestAadhaarNo = rsaService.decodeMessage(verifyOTPReqeustDTO.getAadhaarNo());

        if (!(decodedRequestAadhaarNo.matches(aadhaarRegex))) {
            throw new RegexMisMatchException("Please Provide Valid Aadhaar Number");
        }

        UserKYCDetails userKYCDetails = userKYCDetailsService.getUserKYCDetailsByUserId(userId);
        if (userKYCDetails == null) {
            throw new PreviousStepsNotDoneException("Please First Verify Mobile NO");
        }

        String decodedSavedAadhaarNo = rsaService.decodeMessage(userKYCDetails.getAadharNo());

        if (!(decodedRequestAadhaarNo.equals(decodedSavedAadhaarNo))) {
            throw new WrongAadhaarNoException("Please Provide Valid Aadhaar No");
        }
        String accessToken = getAccessToken();
        AadhaarOTP aadhaarOTP = retrofitAadhaarOTP.create(AadhaarOTP.class);
        Response<TransactionIDResponseDTO> response;
        try {
            response = aadhaarOTP.verifyOTP(verifyOTPReqeustDTO, "Bearer " + accessToken).execute();

        } catch (IOException e) {
            throw new ExternalServiceException("Error occurred during aadhaar OTP verification");
        }
        if (response.code() != 200) {
            throw new WrongOTPException("Please Provide Valid OTP");
        }
        userDetailsService.updateUserStatus(userId,UserOnBoardingStatus.VERIFY_AADHAR);
    }

    @Override
    public void verifyAadhaarPanLinkStatus(String panNo, String aadhaarNo) {

        AadhaarPANInfoDTO aadhaarPanInfoDTO = new AadhaarPANInfoDTO(aadhaarNo, panNo);
        AadhaarPANLink aadhaarPANLink = retrofitPanVerification.create(AadhaarPANLink.class);
        Call<AadhaarPANLinkResponseDTO> aadhaarPanLinkStatus = aadhaarPANLink.getAadharPANLinkStatus(aadhaarPanInfoDTO, userServiceConfigurationProperties.getRapidApiKey());
        Response<AadhaarPANLinkResponseDTO> response;

        String statusCode = "";
        try {
            response = aadhaarPanLinkStatus.execute();
        } catch (IOException e) {
            throw new ExternalServiceException("Error occurred during PAN and Aadhaar Link verification");
        }
        if (response.code() == 200) {
            AadhaarPANLinkResponseDTO aadhaarPanLinkResponseDTO = response.body();
            if (aadhaarPanLinkResponseDTO != null && (aadhaarPanLinkResponseDTO.getMessages() != null) && (!aadhaarPanLinkResponseDTO.getMessages().isEmpty())) {
                statusCode = aadhaarPanLinkResponseDTO.getMessages().get(0).getCode();
            }
        } else {
            throw new ExternalServiceException("Error occurred during PAN and Aadhaar Link verification");
        }
        if (!statusCode.equals("EF40124")) {
            throw new AadhaarPANLinkException("Aadhar Is Not Linked With Pan");
        }

    }

    @Override
    public void saveNameFromPan(String panNo, UserDetails userDetails) {

        PANInformation panInformation = retrofitPanInformation.create(PANInformation.class);
        PANRequestDTO panRequestDTO = new PANRequestDTO(panNo);
        PANInformationRequestDTO panInformationRequestDTO = new PANInformationRequestDTO(userServiceConfigurationProperties.getRapidApiTaskId(), userServiceConfigurationProperties.getRapidApiGroupId(), panRequestDTO);
        Call<PANInformationResponseDTO> panInformationResponse = panInformation.getAadharPanLinkStatus(panInformationRequestDTO, userServiceConfigurationProperties.getRapidApiKey());

        Response<PANInformationResponseDTO> panInformationResponseDTOResponse;
        try {
            panInformationResponseDTOResponse = panInformationResponse.execute();
        } catch (IOException e) {
            throw new ExternalServiceException("Error occurred during PAN verification");
        }

        PANInformationResponseDTO panDetails = panInformationResponseDTOResponse.body();
        if (panInformationResponseDTOResponse.code() != 200 || panDetails == null) {
            throw new ExternalServiceException("Error occurred during PAN verification");
        }

        if (panDetails.getStatus().equals("failed")) {
            throw new PANInformationVerifyException(panDetails.getMessage());
        }

        SourceOutput sourceOutput = panDetails.getResult().getSource_output();
        userDetails.setFirstName(sourceOutput.getFirst_name());
        userDetails.setLastName(sourceOutput.getLast_name());


    }

    @Override
    @Transactional
    public void verifyPan(String panNo, UUID userId) {

        String decodedPanNo = rsaService.decodeMessage(panNo);

        if (!(decodedPanNo.matches(panRegex))) {
            throw new RegexMisMatchException("Please Provide Valid Pan No");
        }

        UserDetails userDetails = userDetailsService.getUserDetails(userId);
        if (!userDetails.getOnBoardingStatus().equals(UserOnBoardingStatus.VERIFY_AADHAR)) {
            throw new PreviousStepsNotDoneException("Please First Verify Aadhaar");
        }

        UserKYCDetails userKYCDetails = userKYCDetailsService.getUserKYCDetailsByUserId(userId);
        String decodedAadhaarNo = rsaService.decodeMessage(userKYCDetails.getAadharNo());

        verifyAadhaarPanLinkStatus(decodedPanNo, decodedAadhaarNo);
        saveNameFromPan(decodedPanNo, userDetails);
        userKYCDetails.setPanNo(panNo);
        userKYCDetailsService.saveUserKYCDetailsByUserId(userKYCDetails);
        userDetails.setOnBoardingStatus(UserOnBoardingStatus.VERIFY_PAN);
        userDetailsService.updateUserDetails(userDetails);
    }
}
