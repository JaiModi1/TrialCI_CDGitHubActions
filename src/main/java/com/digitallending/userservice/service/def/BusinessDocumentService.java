package com.digitallending.userservice.service.def;

import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.model.dto.business.BusinessDocumentDTO;
import com.digitallending.userservice.model.dto.business.BusinessDocumentDetailsDTO;
import com.digitallending.userservice.model.dto.business.BusinessDocumentTypeDTO;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BusinessDocumentService {


    List<BusinessDocumentDTO> getDocumentsByUserId(UUID userId);

    List<BusinessDocumentTypeDTO> getRemainingDocuments(UUID userId, UUID businessTypeId);

    MsmeBusinessDocument saveDocument(MultipartFile document, UUID businessDocumentTypeId, UUID userId) throws DetailsNotFoundException, IOException;

    MsmeBusinessDocument getDocumentByBusinessDocumentId(UUID businessDocumentId);

    MsmeBusinessDocument updateDocument(MultipartFile document, UUID businessDocumentId, UUID userId) throws IOException;

    void changeBusinessType(BusinessType oldType, BusinessType newType, UUID userId);

    void initializeDatabase();

    BusinessDocumentDetailsDTO getBusinessDocumentDetailsByUserId(UUID userId);
}
