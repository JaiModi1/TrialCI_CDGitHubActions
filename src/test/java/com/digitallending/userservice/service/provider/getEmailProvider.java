package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.model.entity.ChangePasswordToken;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class getEmailProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        ChangePasswordToken changePasswordToken = ChangePasswordToken
                .builder()
                .token(UUID.randomUUID())
                .email("tem@gmail.com")
                .build();
        Optional<ChangePasswordToken> correctResult = Optional.of(changePasswordToken);
        Optional<ChangePasswordToken> nullResult = Optional.ofNullable(null);
        return Stream.of(
                Arguments.of(correctResult, false),
                Arguments.of(nullResult, true)
        );
    }
}
