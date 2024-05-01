package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.model.entity.UserDetails;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class getUserStatusProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        UserDetails user = UserDetails.builder().userId(UUID.randomUUID()).onBoardingStatus(UserOnBoardingStatus.VERIFY_PAN).build();

        Optional<UserDetails> correctUser = Optional.of(user);
        Optional<UserDetails> nullUser = Optional.ofNullable(null);

        return Stream.of(
                Arguments.of(correctUser, false),
                Arguments.of(nullUser, true)
        );
    }
}
