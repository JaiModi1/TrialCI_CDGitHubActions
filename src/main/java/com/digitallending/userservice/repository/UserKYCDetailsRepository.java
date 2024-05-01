package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.UserKYCDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserKYCDetailsRepository extends JpaRepository<UserKYCDetails, UUID> {
}
