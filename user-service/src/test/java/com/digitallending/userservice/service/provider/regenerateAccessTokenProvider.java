package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.model.dto.userregistration.SignInResponseDTO;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import retrofit2.Response;

import java.util.stream.Stream;

public class regenerateAccessTokenProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        SignInResponseDTO signInResponseDTO = SignInResponseDTO.builder()
                .accessToken("accessToken")
                .refreshToken("RefreshToken")
                .build();

        Response<SignInResponseDTO> invalidResponse = Response.error(400, ResponseBody.create(null,"Fail"));
        Response<SignInResponseDTO> validResponse = Response.success(signInResponseDTO);
        return Stream.of(
                Arguments.of(invalidResponse),
                Arguments.of(validResponse)
        );
    }
}
