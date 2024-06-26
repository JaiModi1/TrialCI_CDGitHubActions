package com.digitallending.userservice.model.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SendTransactionRequestDto {
    @NotNull
    private UUID userId;
}