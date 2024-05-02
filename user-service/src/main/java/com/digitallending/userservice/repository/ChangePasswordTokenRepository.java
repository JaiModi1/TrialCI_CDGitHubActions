package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.ChangePasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChangePasswordTokenRepository extends JpaRepository<ChangePasswordToken, UUID> {
}
