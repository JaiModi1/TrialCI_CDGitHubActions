package com.digitallending.userservice.model.dto.business;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BusinessDocumentDetailsDTO {
    private List<BusinessDocumentDTO> listOfSubmittedDocs;
    private List<BusinessDocumentTypeDTO> listOfRemainingDocs;
}
