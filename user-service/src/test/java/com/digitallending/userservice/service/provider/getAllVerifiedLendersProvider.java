package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.model.entity.UserDetails;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class getAllVerifiedLendersProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        UserDetails user1 = UserDetails
                .builder()
                .userId(UUID.randomUUID())
                .firstName("abcd")
                .lastName("efgh")
                .email("tem@gmail.com")
                .build();
        UserDetails user2 = UserDetails
                .builder()
                .userId(UUID.randomUUID())
                .firstName("abcd")
                .lastName("efgh")
                .email("tem@gmail.com")
                .build();
        UserDetails user3 = UserDetails
                .builder()
                .userId(UUID.randomUUID())
                .firstName("abcd")
                .lastName("efgh")
                .email("tem@gmail.com")
                .build();
        List<UserDetails> list = Arrays.asList(user1,user2, user3);
        return Stream.of(
                Arguments.of(list)
        );
    }
}
