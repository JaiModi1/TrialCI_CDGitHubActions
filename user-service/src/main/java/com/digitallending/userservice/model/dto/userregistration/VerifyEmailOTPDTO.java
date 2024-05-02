package com.digitallending.userservice.model.dto.userregistration;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VerifyEmailOTPDTO {

    @NotNull
    @Pattern(regexp = "^[0-9]{6}$", message = "Please Provide Valid OTP")
    private String otp;


}
