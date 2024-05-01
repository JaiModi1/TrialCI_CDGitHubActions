package com.digitallending.userservice.model.dto.kyc.mobileotp;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOTPDTO {
    @NotNull
    @Pattern(regexp = "^[0-9]{6}$", message = "Please Provide Valid OTP")
    private String otp;
}
