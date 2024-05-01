package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.model.dto.userregistration.SignUpDTO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class validateUserSignUpRequestProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        SignUpDTO validRequest = SignUpDTO
                .builder()
                .dob(new Date())
                .role(Role.MSME)
                .confirmPassword("aA@12345")
                .email("temp@gmail.com")
                .password("aA@12345")
                .build();
        SignUpDTO invalidAdminRequest = SignUpDTO
                .builder()
                .dob(new Date())
                .role(Role.ADMIN)
                .confirmPassword("aA@12345")
                .email("temp@gmail.com")
                .password("aA@12345")
                .build();
        SignUpDTO invalidRegExRequest = SignUpDTO
                .builder()
                .dob(new Date())
                .role(Role.ADMIN)
                .confirmPassword("abcd")
                .email("temp@gmail.com")
                .password("abcd")
                .build();
        SignUpDTO invalidPasswordRequest = SignUpDTO
                .builder()
                .dob(new Date())
                .role(Role.ADMIN)
                .confirmPassword("aA@12345")
                .email("temp@gmail.com")
                .password("aC$12345")
                .build();

        List<UserRepresentation> emptyList = new ArrayList<>();
        List<UserRepresentation> entryInList = Arrays.asList(new UserRepresentation());
        return Stream.of(
                Arguments.of(
                        validRequest,
                        emptyList,
                        false,
                        entryInList
                ),
                Arguments.of(
                        invalidAdminRequest,
                        emptyList,
                        false,
                        entryInList
                ),
                Arguments.of(
                        validRequest,
                        entryInList,
                        false,
                        entryInList
                ),
                Arguments.of(
                        invalidRegExRequest,
                        entryInList,
                        true,
                        entryInList
                ),
                Arguments.of(
                        invalidPasswordRequest,
                        entryInList,
                        false,
                        entryInList
                )
        );
    }
}
