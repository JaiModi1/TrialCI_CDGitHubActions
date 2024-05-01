package com.digitallending.userservice.model.dto.userregistration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ResetPasswordDTO {
    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;
    private UUID token;
}
