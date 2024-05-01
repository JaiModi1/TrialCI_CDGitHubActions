package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BusinessTypeRepository extends JpaRepository<BusinessType, UUID> {
    Optional<BusinessType> findByBusinessType(String businessType);
}
