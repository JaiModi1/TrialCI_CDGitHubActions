package com.digitallending.userservice.service.def;

public interface TwilioService {
    String sendMobileOTP(String mobileNo);

    void verifyMobileOTP(String mobileNo, String otp);

    void verifyMobileOTPByVerificationSID(String sid, String otp);
}
