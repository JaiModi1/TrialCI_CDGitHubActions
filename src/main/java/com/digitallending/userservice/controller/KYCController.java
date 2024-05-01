package com.digitallending.userservice.controller;

import com.digitallending.userservice.model.dto.apiresponse.APIResponseDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.AadhaarDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.TransactionIDResponseDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.VerifyOTPReqeustDTO;
import com.digitallending.userservice.model.dto.kyc.mobileotp.MobileNoDTO;
import com.digitallending.userservice.model.dto.kyc.mobileotp.VerifyMobileOTPDTO;
import com.digitallending.userservice.model.dto.kyc.paninformation.PANDTO;
import com.digitallending.userservice.model.dto.transaction.SendTransactionRequestDto;
import com.digitallending.userservice.model.dto.transaction.VerifyTransactionRequestDTO;
import com.digitallending.userservice.service.def.KYCService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class KYCController {

    @Autowired
    private KYCService kycService;

    @PostMapping("/generate-mobile-otp")
    public ResponseEntity<APIResponseDTO<String>> generateMobileOTP(@Valid @RequestBody MobileNoDTO mobileNoDTO, @RequestHeader("UserId") UUID userId) {

        kycService.sendMobileOtp(mobileNoDTO.getMobileNo(), userId);
        APIResponseDTO<String> apiResponseDTO = APIResponseDTO.<String>builder()
                .payload("OTP successfully sent.")
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/verify-mobile-otp")
    public ResponseEntity<APIResponseDTO<String>> verifyMobileOTP(@Valid @RequestBody VerifyMobileOTPDTO verifyMobileOTPDTO, @RequestHeader("UserId") UUID userId) {
        kycService.verifyMobileOtp(verifyMobileOTPDTO, userId);
        APIResponseDTO<String> apiResponseDTO = APIResponseDTO.<String>builder()
                .payload("OTP Verification Successfully Completed")
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/verify-pan")
    public ResponseEntity<APIResponseDTO<String>> verifyPan(@Valid @RequestBody PANDTO panDTO, @RequestHeader("UserId") UUID userId) {
        kycService.verifyPan(panDTO.getPanNo(), userId);
        APIResponseDTO<String> apiResponseDTO = APIResponseDTO.<String>builder()
                .payload("Pan Verification Successfully Completed")
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/generate-aadhaar-otp")
    public ResponseEntity<APIResponseDTO<TransactionIDResponseDTO>> generateAadhaarOTP(@RequestHeader("UserId") UUID userId, @Valid @RequestBody AadhaarDTO aadhaarDTO) {
        TransactionIDResponseDTO transactionIDResponseDTO = kycService.sendAadhaarOTP(aadhaarDTO, userId);
        APIResponseDTO<TransactionIDResponseDTO> apiResponseDTO = APIResponseDTO.<TransactionIDResponseDTO>builder()
                .payload(transactionIDResponseDTO)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("verify-aadhaar-otp")
    public ResponseEntity<APIResponseDTO<String>> verifyAadhaarOTP(@Valid @RequestBody VerifyOTPReqeustDTO verifyOTPReqeustDTO, @RequestHeader("UserId") UUID userId) {
        kycService.verifyAadhaarOTP(verifyOTPReqeustDTO, userId);
        APIResponseDTO<String> apiResponseDTO = APIResponseDTO.<String>builder()
                .payload("OTP Verification Successfully Completed")
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/transaction-otp")
    public ResponseEntity<APIResponseDTO<String>> generateMobileOTPLoanDisbursal(@Valid @RequestBody SendTransactionRequestDto sendTransactionRequestDto) {
        String sid = kycService.sendMobileOtpLoanDisbursal(sendTransactionRequestDto.getUserId());
        APIResponseDTO<String> apiResponseDTO = APIResponseDTO.<String>builder()
                .payload(sid)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/verify-transaction-otp")
    public ResponseEntity<APIResponseDTO<String>> verifyMobileOTPLoanDisbursal(@Valid @RequestBody VerifyTransactionRequestDTO verifyTransactionRequestDTO) {
        kycService.verifyMobileOtpLoanDisbursal(verifyTransactionRequestDTO);
        APIResponseDTO<String> apiResponseDTO = APIResponseDTO.<String>builder()
                .payload("OTP Verification Successfully Completed")
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }
}
