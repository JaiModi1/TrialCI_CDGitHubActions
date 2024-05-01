package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.msmeuser.UserDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface MsmeUserDocumentTypeRepository extends JpaRepository<UserDocumentType, UUID> {
}
