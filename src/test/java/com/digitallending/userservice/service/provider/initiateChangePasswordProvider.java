package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.model.dto.userregistration.ChangePasswordDTO;
import com.digitallending.userservice.model.dto.userregistration.SignInResponseDTO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class initiateChangePasswordProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        SignInResponseDTO signInResponseDTO = SignInResponseDTO.builder()
                .accessToken("accessToken")
                .refreshToken("RefreshToken")
                .build();
        List<UserRepresentation> listOfUsers = new ArrayList<>();
        UserRepresentation user = new UserRepresentation();
        user.setEmail("temp@gmail.com");
        listOfUsers.add(user);
        ChangePasswordDTO validRequest = ChangePasswordDTO
                .builder()
                .password("aA@12345")
                .confirmPassword("aA@12345")
                .oldPassword("bB$12345")
                .build();
        return Stream.of(
                Arguments.of(
                        listOfUsers,signInResponseDTO,validRequest,false
                )
        );
    }

//    SignInResponseDTO signInResponse,
//    ChangePasswordDTO request,
//    boolean isRegExMissmatch
}
