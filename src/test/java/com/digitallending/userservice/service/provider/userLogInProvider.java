package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.model.dto.userregistration.SignInResponseDTO;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.keycloak.representations.idm.UserRepresentation;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class userLogInProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        List<UserRepresentation> emptyList = new ArrayList<>();
        List<UserRepresentation> filledList = new ArrayList<>();
        UserRepresentation user = new UserRepresentation();
        user.setUsername(String.valueOf(UUID.randomUUID()));
        filledList.add(user);

        SignInResponseDTO signInResponseDTO = SignInResponseDTO.builder()
                .accessToken("accessToken")
                .refreshToken("RefreshToken")
                .build();
        Response<SignInResponseDTO> invalidResponse = Response.error(400, ResponseBody.create(null,"Fail"));
        Response<SignInResponseDTO> emptyResponse = Response.success(null);
        Response<SignInResponseDTO> validResponse = Response.success(signInResponseDTO);
        return Stream.of(
                Arguments.of(
                        "tem@gmail.com","aA@12345",false,filledList,validResponse,false
                ),
                Arguments.of(
                        "tem@gmail.com","a12345",true,filledList,validResponse,false
                ),
                Arguments.of(
                        "tem@gmail.com","aA@12345",false,emptyList,validResponse,false
                ),
                Arguments.of(
                        "tem@gmail.com","aA@12345",false,emptyList,emptyResponse,true
                ),
                Arguments.of(
                        "tem@gmail.com","aA@12345",false,emptyList,invalidResponse,true
                )
        );

    }
}
/*
* String email,
                       String password,
                       boolean isRegExMissmatch,
                       List<UserRepresentation> listOfUserRep,
                       Response<SignInResponseDTO> signInResponse,
                       boolean isAdmin
* */
