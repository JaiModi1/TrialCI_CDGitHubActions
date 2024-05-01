package com.digitallending.userservice.model.dto.business;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BusinessTypeDTO {

    private UUID businessTypeId;

    private String businessType;
}
