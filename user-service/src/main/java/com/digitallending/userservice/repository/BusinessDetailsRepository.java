package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BusinessDetailsRepository extends JpaRepository<MsmeBusinessDetails, UUID> {

    @Query(value = "SELECT ud.business_type_id , COUNT(*) from msme_business_details as ud group by ud.business_type_id", nativeQuery = true)
    List<Object[]> findBusinessTypes();

    Page<MsmeBusinessDetails> findByBusinessTypeBusinessType(String businessType, Pageable pageable);

    long countByBusinessTypeBusinessType(String businessType);

}
