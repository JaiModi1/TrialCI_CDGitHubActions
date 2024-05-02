package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.config.UserServiceConfigurationProperties;
import com.digitallending.userservice.exception.InvalidMobileNumberException;
import com.digitallending.userservice.exception.WrongOTPException;
import com.digitallending.userservice.service.def.TwilioService;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioServiceImpl implements TwilioService {

    @Autowired
    private UserServiceConfigurationProperties userServiceConfigurationProperties;

    @PostConstruct
    public void init() {
        Twilio.init(userServiceConfigurationProperties.getTwilioAccountSid(), userServiceConfigurationProperties.getTwilioAuthToken());
    }

    @Override
    public String sendMobileOTP(String mobileNo) {
        Verification verification;
        try {
            verification = Verification.creator(userServiceConfigurationProperties.getTwilioVerifySid(), mobileNo, "sms").create();
        } catch (ApiException exception) {
            throw new InvalidMobileNumberException(exception.getMessage());
        }
        return verification.getSid();
    }

    @Override
    public void verifyMobileOTP(String mobileNo, String otp) {
        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(userServiceConfigurationProperties.getTwilioVerifySid()).setTo(mobileNo).setCode(otp).create();
            if (Boolean.FALSE.equals(verificationCheck.getValid())) {
                throw new WrongOTPException("Wrong Otp, Provide Valid OTP");
            }
        } catch (ApiException ex) {
            throw new WrongOTPException(ex.getMessage());
        }
    }

    @Override
    public void verifyMobileOTPByVerificationSID(String sid, String otp) {
        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(userServiceConfigurationProperties.getTwilioVerifySid()).setVerificationSid(sid).setCode(otp).create();
            if (Boolean.FALSE.equals(verificationCheck.getValid())) {
                throw new WrongOTPException("Wrong Otp, Provide Valid OTP");
            }
        } catch (ApiException ex) {
            throw new WrongOTPException(ex.getMessage());
        }
    }
}
