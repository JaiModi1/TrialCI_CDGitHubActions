package com.digitallending.userservice.repository;

import com.digitallending.userservice.model.entity.EmailOTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailOTPRepository extends JpaRepository<EmailOTP, String> {
}
