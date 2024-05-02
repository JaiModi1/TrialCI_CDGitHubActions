package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.BusinessDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessDocumentTypeRepository extends JpaRepository<BusinessDocumentType, UUID> {
    BusinessDocumentType findByBusinessDocumentType(String documentType);
}
