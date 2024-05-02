package com.digitallending.userservice.model.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyTransactionRequestDTO {
    @NotBlank
    private String txnId;
    @NotNull
    @Pattern(regexp = "^[0-9]{6}$", message = "Please Provide Valid OTP")
    private String otp;
}