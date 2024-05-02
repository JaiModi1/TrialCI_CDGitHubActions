package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.exception.WrongOTPException;
import com.digitallending.userservice.model.entity.EmailOTP;
import com.digitallending.userservice.repository.EmailOTPRepository;
import com.digitallending.userservice.service.def.EmailOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;


@Service
public class EmailOTPServiceImpl implements EmailOTPService {
    @Autowired
    private EmailOTPRepository emailOTPRepository;

    @Override
    public void saveOTP(String email, String otp) {
        Instant expirationTimeStamp = Instant.now().plusSeconds(300);
        EmailOTP emailOTP = new EmailOTP();
        emailOTP.setEmailId(email);
        emailOTP.setOtp(otp);
        emailOTP.setExpirationTime(Timestamp.from(expirationTimeStamp));
        emailOTPRepository.save(emailOTP);
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        EmailOTP reference = emailOTPRepository.getReferenceById(email);
        Timestamp current = Timestamp.from(Instant.now());
        int comp = current.compareTo(reference.getExpirationTime());
        if (comp > 0) {
            throw new WrongOTPException("OTP Expired");
        }
        if (reference.getOtp().equals(otp)) {
            emailOTPRepository.delete(reference);
            return true;
        }
        throw new WrongOTPException("The OTP entered is incorrect");
    }


}
