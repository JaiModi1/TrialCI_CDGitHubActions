package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.model.entity.EmailOTP;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.stream.Stream;

public class verifyOTPProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 5);
        EmailOTP correctRefrence = EmailOTP.builder().otp("123455").emailId("Email@gmail.com").expirationTime(new Timestamp(cal.getTimeInMillis())).build();
        EmailOTP incorrectOTP = EmailOTP.builder().otp("000000").emailId("Email@gmail.com").expirationTime(new Timestamp(cal.getTimeInMillis())).build();
        cal.add(Calendar.MINUTE, -5);
        EmailOTP incorreceReference =EmailOTP.builder().otp("113131").emailId("Email@gmail.com").expirationTime(new Timestamp(cal.getTimeInMillis())).build();


        return Stream.of(
                Arguments.of(correctRefrence,false),
                Arguments.of(incorrectOTP, true),
                Arguments.of(incorreceReference,true)
        );
    }
}
