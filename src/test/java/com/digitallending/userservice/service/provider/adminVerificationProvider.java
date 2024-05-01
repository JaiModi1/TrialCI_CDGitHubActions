package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.model.dto.userregistration.ChangeUserStatusDTO;
import com.digitallending.userservice.model.entity.UserDetails;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class adminVerificationProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        ChangeUserStatusDTO verifyRequest = ChangeUserStatusDTO
                .builder()
                .onBoardingStatus(UserOnBoardingStatus.VERIFIED)
                .userId(UUID.randomUUID())
                .build();
        ChangeUserStatusDTO modifyRequest = ChangeUserStatusDTO
                .builder()
                .onBoardingStatus(UserOnBoardingStatus.ON_HOLD)
                .userId(UUID.randomUUID())
                .build();
        ChangeUserStatusDTO failRequest = ChangeUserStatusDTO
                .builder()
                .onBoardingStatus(UserOnBoardingStatus.BANK_DETAILS)
                .userId(UUID.randomUUID())
                .build();

        UserDetails unverifiedUser = UserDetails
                .builder()
                .userId(UUID.randomUUID())
                .onBoardingStatus(UserOnBoardingStatus.UNVERIFIED)
                .build();
        Optional<UserDetails> optionalUnverified = Optional.of(unverifiedUser);

        UserDetails reverifyUser = UserDetails
                .builder()
                .userId(UUID.randomUUID())
                .onBoardingStatus(UserOnBoardingStatus.REVERIFY)
                .build();
        Optional<UserDetails> optionalReverify = Optional.of(reverifyUser);

        UserDetails invalidUser = UserDetails
                .builder()
                .userId(UUID.randomUUID())
                .onBoardingStatus(UserOnBoardingStatus.BUSINESS_DETAILS)
                .build();
        Optional<UserDetails> optionalInvalid = Optional.of(invalidUser);

        return Stream.of(
                Arguments.of(verifyRequest, optionalUnverified, false),
                Arguments.of(verifyRequest, optionalReverify, false),
                Arguments.of(verifyRequest, optionalInvalid, true),
                Arguments.of(modifyRequest, optionalUnverified, false),
                Arguments.of(modifyRequest, optionalReverify, false),
                Arguments.of(modifyRequest, optionalInvalid, true),
                Arguments.of(failRequest, optionalUnverified, true),
                Arguments.of(failRequest, optionalReverify, true),
                Arguments.of(failRequest, optionalInvalid, true)


        );
    }
}
