package com.digitallending.userservice.model.dto.mail;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class MailRequestDTO {
    private UUID userId;
    private String subject;
    private String message;
}