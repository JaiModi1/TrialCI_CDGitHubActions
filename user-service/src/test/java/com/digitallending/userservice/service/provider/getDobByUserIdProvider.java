package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.model.entity.UserDetails;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class getDobByUserIdProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        UserDetails user = UserDetails.builder().userId(UUID.randomUUID()).dob(new Date()).build();
        Optional<UserDetails> correctUser = Optional.ofNullable(user);
        Optional<UserDetails> nullUser = Optional.ofNullable(null);
        return Stream.of(
                Arguments.of(nullUser, true),
                Arguments.of(correctUser,false)

        );
    }
}
