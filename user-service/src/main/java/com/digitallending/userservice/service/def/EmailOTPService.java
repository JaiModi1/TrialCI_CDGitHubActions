package com.digitallending.userservice.service.def;


public interface EmailOTPService {
    void saveOTP(String email, String otp);

    boolean verifyOTP(String email, String otp);

}
