package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MsmeUserDocumentRepository extends JpaRepository<MsmeUserDocument, UUID> {

    Integer countByUserUserId(UUID userId);

    MsmeUserDocument findByUserUserIdAndDocumentTypeDocumentTypeId(UUID userId, UUID documentTypeId);

    List<MsmeUserDocument> findByUserUserId(UUID userId);


}
