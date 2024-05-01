package com.digitallending.userservice.service;


import com.digitallending.userservice.config.RetrofitBuilderConfig;
import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.exception.EmailAlreadyExistsException;
import com.digitallending.userservice.exception.ExternalServiceException;
import com.digitallending.userservice.exception.IncorrectCredentialsException;
import com.digitallending.userservice.exception.InvalidUserException;
import com.digitallending.userservice.exception.RegexMisMatchException;
import com.digitallending.userservice.exception.UnAuthorizedException;
import com.digitallending.userservice.model.dto.keycloak.User;
import com.digitallending.userservice.model.dto.userregistration.GenerateEmailOtpDTO;
import com.digitallending.userservice.model.dto.userregistration.ResetPasswordDTO;
import com.digitallending.userservice.model.dto.userregistration.SignInResponseDTO;
import com.digitallending.userservice.model.dto.userregistration.SignUpDTO;
import com.digitallending.userservice.service.def.KeyCloakLoginService;
import com.digitallending.userservice.service.def.RSAService;
import com.digitallending.userservice.service.def.UserDetailsService;
import com.digitallending.userservice.service.impl.KeyCloakServiceImpl;
import com.digitallending.userservice.service.provider.getUserByEmailProvider;
import com.digitallending.userservice.service.provider.initiateUpdateEmailProvider;
import com.digitallending.userservice.service.provider.regenerateAccessTokenProvider;
import com.digitallending.userservice.service.provider.resetPasswordProvider;
import com.digitallending.userservice.service.provider.userLogInProvider;
import com.digitallending.userservice.service.provider.validateUserSignUpRequestProvider;
import com.digitallending.userservice.utils.KeycloakSecurityUtil;
import com.digitallending.userservice.utils.MailServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KeyCloakServiceTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Keycloak keyCloak;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RetrofitBuilderConfig retrofitBuilderConfig;
    @InjectMocks
    private KeyCloakServiceImpl keyCloakService;
    @Mock
    private KeycloakSecurityUtil keycloakSecurityUtil;
    @Mock
    private MailServiceUtil mailServiceUtil;
    @Mock
    private RSAService rsaService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private Retrofit retrofit;

    private String realm = "realm";
    private String baseURL = "http://localhost:8080";
    private String clientId = "clientId";
    private String clientSecret = "ClientSecret";
    private String defaultRole = "defaultRole";

    private String adminEmail = "adminEmail";
    private String adminPassword = "adminPassword";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(keyCloakService, "realm", realm);
        ReflectionTestUtils.setField(keyCloakService, "baseURL", baseURL);
        ReflectionTestUtils.setField(keyCloakService, "clientId", clientId);
        ReflectionTestUtils.setField(keyCloakService, "clientSecret", clientSecret);
        ReflectionTestUtils.setField(keyCloakService, "defaultRole", defaultRole);
        ReflectionTestUtils.setField(keyCloakService, "adminEmail", adminEmail);
        ReflectionTestUtils.setField(keyCloakService, "adminPassword", adminPassword);
    }

    @ParameterizedTest
    @ArgumentsSource(getUserByEmailProvider.class)
    void getUserByEmailTest(String email, boolean isNull, List<UserRepresentation> listOfUsers) {
        when(keycloakSecurityUtil.getKeycloakInstance()).thenReturn(
                keyCloak);
        when(keyCloak.realm(anyString()).users().searchByEmail(anyString(), anyBoolean())).thenReturn(listOfUsers);

        User user = keyCloakService.getUserByEmail(email);
        if (isNull) {
            assertNull(user);
        } else {
            assertNotNull(user);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(initiateUpdateEmailProvider.class)
    void initiateUpdateMailTest(String email, boolean isPresent, List<UserRepresentation> listOfUsers) {
        when(keycloakSecurityUtil.getKeycloakInstance()).thenReturn(
                keyCloak);
        when(keyCloak.realm(anyString()).users().searchByEmail(anyString(), anyBoolean())).thenReturn(listOfUsers);

        if (!isPresent) {
            keyCloakService.initiateUpdateEmail(GenerateEmailOtpDTO
                    .builder()
                    .email(email)
                    .build());
            verify(mailServiceUtil, times(1))
                    .sendOTPEmail(ArgumentMatchers.any());
        } else {
            assertThrows(InvalidUserException.class,
                    () -> {
                        keyCloakService.initiateUpdateEmail(GenerateEmailOtpDTO
                                .builder()
                                .email(email)
                                .build());
                    }
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(validateUserSignUpRequestProvider.class)
    void validateUserSignUpTest(
            SignUpDTO request,
            List<UserRepresentation> listOfUsers,
            boolean isRegExMissmatch,
            List<UserRepresentation> newSetOfUsers) {
        when(keycloakSecurityUtil.getKeycloakInstance()).thenReturn(
                keyCloak);
        when(keyCloak
                .realm(anyString())
                .users()
                .searchByEmail(anyString(), anyBoolean()))
                .thenReturn(listOfUsers);
        if (request.getRole().equals(Role.ADMIN)) {
            assertThrows(IncorrectCredentialsException.class,
                    () -> keyCloakService.validateUserSignUpRequest(request));
        } else {
            if (listOfUsers.size() == 0) {
                when(rsaService.decodeMessage(request.getConfirmPassword()))
                        .thenReturn(request.getConfirmPassword());
                when(rsaService.decodeMessage(request.getPassword()))
                        .thenReturn(request.getPassword());
                if (isRegExMissmatch) {
                    assertThrows(RegexMisMatchException.class,
                            () -> keyCloakService.validateUserSignUpRequest(request));
                } else if (!request.getConfirmPassword().equals(request.getPassword())) {
                    assertThrows(InvalidUserException.class,
                            () -> keyCloakService.validateUserSignUpRequest(request));
                } else {

                    when(keyCloak
                            .realm(anyString())
                            .users()
                            .create(any(UserRepresentation.class))).thenReturn(null);

                    when(keyCloak
                            .realm(anyString())
                            .users()
                            .searchByUsername(anyString(), anyBoolean()))
                            .thenReturn(newSetOfUsers);
                    if (newSetOfUsers.isEmpty()) {
                        assertThrows(DetailsNotFoundException.class,
                                () -> keyCloakService.validateUserSignUpRequest(request));
                    }
                    when(keyCloak
                            .realm(anyString())
                            .roles()
                            .get(anyString())
                            .toRepresentation()).thenReturn(new RoleRepresentation());
                    when(keyCloak.realm("realm").users().get("anyString()").roles().realmLevel()).thenReturn(null);

                    when(keyCloak
                            .realm(anyString())
                            .users()
                            .get(anyString())
                            .roles().realmLevel()).thenReturn(null);

                    keyCloakService.validateUserSignUpRequest(request);
                    verify(userDetailsService, times(1)).createUser(
                            ArgumentMatchers.eq(request.getEmail()),
                            ArgumentMatchers.eq(request.getDob()),
                            ArgumentMatchers.eq(request.getRole()),
                            ArgumentMatchers.any()
                    );
                }

            } else {
                assertThrows(EmailAlreadyExistsException.class,
                        () -> keyCloakService.validateUserSignUpRequest(request));
            }
        }
    }

    @Test
    void userLogOutTest() throws IOException {
        KeyCloakLoginService keyCloakApi = mock(KeyCloakLoginService.class);
        when(retrofitBuilderConfig.keyCloakRetrofit().create(KeyCloakLoginService.class))
                .thenReturn(keyCloakApi);
        Call<String> call = mock(Call.class);
        when(keyCloakApi.logout(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(call);
        Response<String> succssfulResponse = Response.success("Logout Successful");
        try {
            when(call.execute()).thenReturn(succssfulResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<UserRepresentation> listOfUsers = new ArrayList<>();
        listOfUsers.add(new UserRepresentation());
        when(keycloakSecurityUtil.getKeycloakInstance()).thenReturn(
                keyCloak);
        when(keyCloak
                .realm(anyString())
                .users()
                .searchByUsername(anyString(), anyBoolean()))
                .thenReturn(listOfUsers);
        when(keyCloak.realm(anyString()).users().get(anyString())).thenReturn(null);
        assertTrue(keyCloakService.userLogOut("abcd", UUID.randomUUID()));
    }

    @ParameterizedTest
    @ArgumentsSource(userLogInProvider.class)
    void userLoginTest(String email,
                       String password,
                       boolean isRegExMissmatch,
                       List<UserRepresentation> listOfUserRep,
                       Response<SignInResponseDTO> signInResponse,
                       boolean isAdmin) throws IOException {
        when(rsaService.decodeMessage(password)).thenReturn(password);
        if (isRegExMissmatch) {
            assertThrows(RegexMisMatchException.class,
                    () -> keyCloakService.userLogIn(email, password));
        } else {
            when(keycloakSecurityUtil.getKeycloakInstance()).thenReturn(
                    keyCloak);
            when(keyCloak
                    .realm(anyString())
                    .users()
                    .searchByEmail(anyString(), anyBoolean()))
                    .thenReturn(listOfUserRep);
            if (listOfUserRep.isEmpty()) {
                assertThrows(IncorrectCredentialsException.class,
                        () -> keyCloakService.userLogIn(email, password));
            } else {
                KeyCloakLoginService keyCloakApi = mock(KeyCloakLoginService.class);
                when(retrofitBuilderConfig.keyCloakRetrofit().create(KeyCloakLoginService.class))
                        .thenReturn(keyCloakApi);
                Call<SignInResponseDTO> call = mock(Call.class);
                when(keyCloakApi.getToken(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString()))
                        .thenReturn(call);
                try {
                    when(call.execute()).thenReturn(signInResponse);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (!signInResponse.isSuccessful()) {
                    assertThrows(IncorrectCredentialsException.class,
                            () -> keyCloakService.userLogIn(email, password));
                } else if (signInResponse.body() == null) {
                    assertThrows(ExternalServiceException.class,
                            () -> keyCloakService.userLogIn(email, password));
                } else {
                    if (!isAdmin) {
                        when(userDetailsService.getUserStatus(any(UUID.class)))
                                .thenReturn(UserOnBoardingStatus.BANK_DETAILS);
                        SignInResponseDTO result = keyCloakService
                                .userLogIn(email, password);
                        assertNotNull(result);
                        assertEquals(result.getOnBoardingStatus(), UserOnBoardingStatus.BANK_DETAILS);
                    }
                    SignInResponseDTO result = keyCloakService
                            .userLogIn(email, password);
                    assertNotNull(result);
                }
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(regenerateAccessTokenProvider.class)
    void regenerateAccessTokenTest(Response<SignInResponseDTO> signInResponse) throws IOException {
        KeyCloakLoginService keyCloakApi = mock(KeyCloakLoginService.class);
        when(retrofitBuilderConfig.keyCloakRetrofit().create(KeyCloakLoginService.class))
                .thenReturn(keyCloakApi);
        Call<SignInResponseDTO> call = mock(Call.class);
        when(keyCloakApi.regenerateToken(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(call);
        try {
            when(call.execute()).thenReturn(signInResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!signInResponse.isSuccessful()) {
            assertThrows(UnAuthorizedException.class,
                    () -> keyCloakService.regenerateAccessToken("abcd"));
        } else {
            SignInResponseDTO result = keyCloakService.regenerateAccessToken("abcd");
            assertNotNull(result);
        }
    }

//    @ParameterizedTest
//    @ArgumentsSource(initiateChangePasswordProvider.class)
//    void initiaiteChangePasswordTest(
//            List<UserRepresentation> listOfUsers,
//            SignInResponseDTO signInResponse,
//            ChangePasswordDTO request,
//            boolean isRegExMissmatch) throws IOException {
//        when(keycloakSecurityUtil.getKeycloakInstance()).thenReturn(keyCloak);
//        when(keyCloak
//                .realm(anyString())
//                .users()
//                .searchByUsername(anyString(), anyBoolean()))
//                .thenReturn(listOfUsers);
//        KeyCloakServiceImpl spy = Mockito.spy(keyCloakService);
//        doReturn(signInResponse).when(spy).userLogIn(anyString(),anyString());
//        when(rsaService
//                .decodeMessage(request.getPassword()))
//                .thenReturn(request.getPassword());
//        when(rsaService
//                .decodeMessage(request.getPassword()))
//                .thenReturn(request.getConfirmPassword());
//
//        if(isRegExMissmatch){
//            assertThrows(RegexMisMatchException.class,
//                    () -> spy.initiateChangePassword(request, UUID.randomUUID()));
//        }
//        else if(!request.getConfirmPassword().equals(request.getPassword())){
//            assertThrows(IncorrectCredentialsException.class,
//                    () -> spy.initiateChangePassword(request, UUID.randomUUID()));
//        }
//        else{
//            verify(spy).changePassword(
//                    ArgumentMatchers.eq(request.getPassword()),
//                    ArgumentMatchers.any());
//        }
//
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(resetPasswordProvider.class)
//    void resetPasswordTest(
//            ResetPasswordDTO request,
//            boolean isRegExMissmatch
//            , List<UserRepresentation> listOfUsers) {
//        when(rsaService.decodeMessage(request.getPassword()))
//                .thenReturn(request.getPassword());
//        when(rsaService.decodeMessage(request.getConfirmPassword()))
//                .thenReturn(request.getConfirmPassword());
//
//        if (isRegExMissmatch) {
//            assertThrows(RegexMisMatchException.class,
//                    () -> keyCloakService.resetPassword(request, "abcd"));
//        } else if (!request.getPassword().equals(request.getConfirmPassword())) {
//            assertThrows(IncorrectCredentialsException.class,
//                    () -> keyCloakService.resetPassword(request, "abcd"));
//        } else {
//            when(keycloakSecurityUtil.getKeycloakInstance())
//                    .thenReturn(keyCloak);
//            when(keyCloak.realm(anyString()).users().searchByEmail(anyString(), anyBoolean()))
//                    .thenReturn(listOfUsers);
//            when(keyCloak
//                    .realm(anyString())
//                    .users().get(anyString())).thenReturn(null);
//        }
//    }

}
