package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.model.entity.UserDetails;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.UUID;
import java.util.stream.Stream;

public class updateUserDetailsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        UserDetails user = UserDetails.builder().userId(UUID.randomUUID()).build();
        return Stream.of(
                Arguments.of(user)
        );
    }
}
