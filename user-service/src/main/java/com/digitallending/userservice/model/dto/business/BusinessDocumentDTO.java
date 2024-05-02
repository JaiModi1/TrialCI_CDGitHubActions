package com.digitallending.userservice.model.dto.business;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDocumentDTO {
    private byte[] documentContent;
    private BusinessDocumentTypeDTO businessDocumentType;
    private UUID businessDocumentId;
}
