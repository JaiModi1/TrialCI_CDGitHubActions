package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsAttributeDTO;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MsmeUserDetailsAttributeRepository extends JpaRepository<MsmeUserDetailsAttribute, UUID> {
    MsmeUserDetailsAttribute findByAttributeName(String attributeName);
}
