package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MsmeUserDetailsAttributeValueRepository extends JpaRepository<MsmeUserDetailsAttributeValue, UUID> {
    MsmeUserDetailsAttributeValue findByValue(String value);
}
