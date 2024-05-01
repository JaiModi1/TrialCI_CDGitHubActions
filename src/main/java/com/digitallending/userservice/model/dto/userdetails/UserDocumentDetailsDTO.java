package com.digitallending.userservice.model.dto.userdetails;

import com.digitallending.userservice.model.entity.msmeuser.UserDocumentType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDocumentDetailsDTO {
    private List<MsmeUserDocumentDTO> submittedDocuments;
    private List<UserDocumentType> remainingDocuments;
}
