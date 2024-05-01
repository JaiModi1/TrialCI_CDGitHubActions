package com.digitallending.userservice.service.def;

import com.digitallending.userservice.model.dto.kyc.aadharotp.AadhaarDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.TransactionIDResponseDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.VerifyOTPReqeustDTO;
import com.digitallending.userservice.model.dto.kyc.mobileotp.VerifyMobileOTPDTO;
import com.digitallending.userservice.model.dto.transaction.VerifyTransactionRequestDTO;
import com.digitallending.userservice.model.entity.UserDetails;

import java.util.UUID;

public interface KYCService {

    void sendMobileOtp(String mobileNo, UUID userId);

    void verifyMobileOtp(VerifyMobileOTPDTO verifyMobileOTPDTO, UUID userID);

    String sendMobileOtpLoanDisbursal(UUID userId);

    void verifyMobileOtpLoanDisbursal(VerifyTransactionRequestDTO request);

    TransactionIDResponseDTO sendAadhaarOTP(AadhaarDTO aadharDTO, UUID userID);

    void verifyAadhaarOTP(VerifyOTPReqeustDTO verifyOTPReqeustDTO, UUID userId);

    void verifyAadhaarPanLinkStatus(String panNo, String aadhaarNo);

    void saveNameFromPan(String panNo, UserDetails userDetails);

    void verifyPan(String panNo, UUID userId);
}
