package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BusinessDocumentRepository extends JpaRepository<MsmeBusinessDocument, UUID> {


    Integer countByUserUserId(UUID userId);

    void deleteByUserUserIdAndBusinessDocumentTypeBusinessDocumentTypeId(UUID userId, UUID documentTypeId);

    List<MsmeBusinessDocument> findByUserUserId(UUID userId);

    MsmeBusinessDocument findByUserUserIdAndBusinessDocumentTypeBusinessDocumentTypeId(UUID userId, UUID documentTypeId);

}

