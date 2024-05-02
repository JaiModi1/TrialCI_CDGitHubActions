package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class updatUserStatusProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(UserOnBoardingStatus.VERIFY_EMAIL),
                Arguments.of(UserOnBoardingStatus.VERIFY_PHONE),
                Arguments.of(UserOnBoardingStatus.VERIFY_AADHAR),
                Arguments.of(UserOnBoardingStatus.VERIFY_PAN),
                Arguments.of(UserOnBoardingStatus.USER_DETAILS),
                Arguments.of(UserOnBoardingStatus.USER_DOC),
                Arguments.of(UserOnBoardingStatus.BUSINESS_DETAILS),
                Arguments.of(UserOnBoardingStatus.BUSINESS_DOC),
                Arguments.of(UserOnBoardingStatus.BANK_DETAILS),
                Arguments.of(UserOnBoardingStatus.UNVERIFIED),
                Arguments.of(UserOnBoardingStatus.VERIFIED),
                Arguments.of(UserOnBoardingStatus.ON_HOLD),
                Arguments.of(UserOnBoardingStatus.REVERIFY)

        );
    }
}
