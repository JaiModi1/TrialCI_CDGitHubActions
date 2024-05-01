package com.digitallending.userservice.service.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class initiateUpdateEmailProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        List<UserRepresentation> emptyList = new ArrayList<>();
        List<UserRepresentation> filledList = Arrays.asList(new UserRepresentation());
        return Stream.of(
                Arguments.of("systemadmin@gmail.com", true, filledList),
                Arguments.of("false@gmail.com", false, emptyList)
        );
    }
}
