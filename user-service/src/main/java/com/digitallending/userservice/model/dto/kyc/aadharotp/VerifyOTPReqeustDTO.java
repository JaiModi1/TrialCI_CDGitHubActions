package com.digitallending.userservice.model.dto.kyc.aadharotp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOTPReqeustDTO {
    @NotNull
    @Pattern(regexp = "^[0-9]{6}$", message = "Please Provide Valid OTP")
    private String otp;


    @NotBlank(message = "Please Provide txnId")
    private String txnId;

    @NotBlank(message = "Please Provide Aadhaar No")
    private String aadhaarNo;
}
