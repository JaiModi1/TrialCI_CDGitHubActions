package com.digitallending.userservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_otp")
public class EmailOTP {
    @Id
    @Column(name = "email_id")
    private String emailId;

    @Column(length = 6)
    private String otp;

    @Column(name = "expiration_time")
    private Timestamp expirationTime;
}
