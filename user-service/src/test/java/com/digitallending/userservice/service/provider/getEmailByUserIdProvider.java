package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.model.entity.UserDetails;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Optional;
import java.util.stream.Stream;

public class getEmailByUserIdProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        UserDetails user = UserDetails.builder().email("temp@gmail.com").build();
        UserDetails user2 = UserDetails.builder().email("email@gmail.com").build();
        Optional<UserDetails> optionalUser = Optional.ofNullable(user);
        Optional<UserDetails> optionalUser2 = Optional.ofNullable(user2);
        Optional<UserDetails> nullUser = Optional.ofNullable(null);
        return Stream.of(
                Arguments.of(optionalUser, false),
                Arguments.of(optionalUser2, false),
                Arguments.of(nullUser, true)
        );
    }
}
