package com.digitallending.userservice.service.def;

import com.digitallending.userservice.model.dto.userregistration.SignInResponseDTO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface KeyCloakLoginService {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("/realms/{realm}/protocol/openid-connect/token")
    Call<SignInResponseDTO> getToken(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("username") String username,
            @Field("password") String password,
            @retrofit2.http.Path("realm") String realm
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("/realms/{realm}/protocol/openid-connect/token")
    Call<SignInResponseDTO> regenerateToken(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("refresh_token") String refreshToken,
            @retrofit2.http.Path("realm") String realm
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("/realms/{realm}/protocol/openid-connect/logout")
    Call<String> logout(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("refresh_token") String refreshToken,
            @retrofit2.http.Path("realm") String realm
    );

}
