package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetails;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttributeValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MsmeUserDetailsRepository extends JpaRepository<MsmeUserDetails, UUID> {
    Page<MsmeUserDetails> findByEducationalQualification(MsmeUserDetailsAttributeValue msmeUserDetailsAttributeValue, Pageable pageable);

    Page<MsmeUserDetails> findByCategory(MsmeUserDetailsAttributeValue attribteId, Pageable pageable);
}
