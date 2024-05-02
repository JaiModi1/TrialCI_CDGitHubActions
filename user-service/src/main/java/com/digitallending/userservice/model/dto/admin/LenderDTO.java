package com.digitallending.userservice.model.dto.admin;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LenderDTO {
    private UUID userId;
    private String fullName;
    private String email;
}
