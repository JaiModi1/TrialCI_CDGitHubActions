package com.digitallending.userservice.model.dto.userdetails;

import com.digitallending.userservice.model.entity.msmeuser.UserDocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MsmeUserDocumentDTO {

    private UserDocumentType documentType;

    private byte[] documentContent;
}
