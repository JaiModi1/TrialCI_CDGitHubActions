package com.digitallending.userservice.service.def;


import com.digitallending.userservice.model.dto.userdetails.MsmeUserDocumentDTO;
import com.digitallending.userservice.model.dto.userdetails.UserDocumentDetailsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MsmeUserDocumentService {

    MsmeUserDocumentDTO updateDocument(UUID userId, UUID documentTypeId, MultipartFile file);

    UserDocumentDetailsDTO getAllDocumentByUserId(UUID userId);

    void initializeDatabase();

}
