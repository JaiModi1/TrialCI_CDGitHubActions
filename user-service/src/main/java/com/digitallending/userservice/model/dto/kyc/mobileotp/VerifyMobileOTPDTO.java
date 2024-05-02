package com.digitallending.userservice.model.dto.kyc.mobileotp;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyMobileOTPDTO {

    @NotNull
    @Pattern(regexp = "^\\+91[1-9][0-9]{9}$", message = "Please Provide Valid Mobile Number")
    private String mobileNumber;

    @NotNull
    @Pattern(regexp = "^[0-9]{6}$", message = "Please Provide Valid OTP")
    private String otp;
}
