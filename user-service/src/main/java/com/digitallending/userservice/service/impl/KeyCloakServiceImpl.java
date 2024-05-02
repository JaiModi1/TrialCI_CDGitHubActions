package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.config.RetrofitBuilderConfig;
import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.exception.EmailAlreadyExistsException;
import com.digitallending.userservice.exception.ExternalServiceException;
import com.digitallending.userservice.exception.IncorrectCredentialsException;
import com.digitallending.userservice.exception.InvalidUserException;
import com.digitallending.userservice.exception.MailNotSentException;
import com.digitallending.userservice.exception.RegexMisMatchException;
import com.digitallending.userservice.exception.UnAuthorizedException;
import com.digitallending.userservice.model.dto.keycloak.User;
import com.digitallending.userservice.model.dto.userregistration.ChangePasswordDTO;
import com.digitallending.userservice.model.dto.userregistration.GenerateEmailOtpDTO;
import com.digitallending.userservice.model.dto.userregistration.ResetPasswordDTO;
import com.digitallending.userservice.model.dto.userregistration.SignInResponseDTO;
import com.digitallending.userservice.model.dto.userregistration.SignUpDTO;
import com.digitallending.userservice.service.def.KeyCloakLoginService;
import com.digitallending.userservice.service.def.KeyCloakService;
import com.digitallending.userservice.service.def.RSAService;
import com.digitallending.userservice.service.def.UserDetailsService;
import com.digitallending.userservice.utils.KeycloakSecurityUtil;
import com.digitallending.userservice.utils.MailServiceUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class KeyCloakServiceImpl implements KeyCloakService {
    private static final String passwordRegEx = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$";
    @Autowired
    private KeycloakSecurityUtil keycloakSecurityUtil;
    @Autowired
    private RetrofitBuilderConfig retrofitBuilderConfig;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private MailServiceUtil mailServiceUtil;
    @Autowired
    private RSAService rsaService;
    @Value("${realm}")
    private String realm;
    @Value("${server-url}")
    private String baseURL;
    @Value("${login-client-id}")
    private String clientId;
    @Value("${login-client-secret}")
    private String clientSecret;
    @Value("${default-role}")
    private String defaultRole;

    @Value("${admin-email}")
    private String adminEmail;
    @Value("${admin-password}")
    private String adminPassword;


    // for testing purposes
//    public void setMailServiceUtil(MailServiceUtil mailServiceUtil){
//        System.out.println("Yea kyu huva");
//        this.mailServiceUtil = mailServiceUtil;
//    }
    //done
    public User getUserByEmail(String emailId) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<User> listOfUsers = keycloak.realm(realm).users().searchByEmail(emailId, true).stream().map(this::userRepToUser).toList();

        if (listOfUsers.isEmpty()) {
            return null;
        }
        return listOfUsers.get(0);
    }

    //done
    @Override
    public boolean checkUserByEmail(String emailId) {
        User user = getUserByEmail(emailId);
        return user != null;
    }

    @Override
    public void initiateUpdateEmail(GenerateEmailOtpDTO request) throws MailNotSentException {
        if (checkUserByEmail(request.getEmail())) {
            throw new InvalidUserException("This email already exits in our database");
        }
        mailServiceUtil.sendOTPEmail(request.getEmail());
    }

    @Override
    public void forgotPassword(GenerateEmailOtpDTO request) throws MailNotSentException {
        if (!checkUserByEmail(request.getEmail())) {
            throw new InvalidUserException("This email does not exits in our database");
        }
        mailServiceUtil.sendOTPEmail(request.getEmail());

    }

    public User getUserByUserName(UUID userId) throws DetailsNotFoundException {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<User> listOfUsers = keycloak.realm(realm).users().searchByUsername(String.valueOf(userId), true).stream().map(this::userRepToUser).toList();
        if (listOfUsers.isEmpty()) {
            throw new DetailsNotFoundException("Can not find the user");
        }
        return listOfUsers.get(0);
    }

    @Override
    public void initiateChangePassword(ChangePasswordDTO request, UUID userId) throws IOException {
        String email = getEmailByUserName(userId);
        userLogIn(email, request.getOldPassword());
        String password = rsaService.decodeMessage(request.getPassword());
        String confirmPassword = rsaService.decodeMessage(request.getConfirmPassword());
        if (!password.matches(passwordRegEx)) {
            throw new RegexMisMatchException("Please enter a password that is 8 characters long with the other requirements");
        }
        if (!password.equals(confirmPassword)) {
            throw new IncorrectCredentialsException("Incorrect password, password and confirm password must be same");
        }

        changePassword(password, getUserByUserName(userId));

    }

    @Override
    public String getEmailByUserName(UUID userId) throws DetailsNotFoundException {
        User user = getUserByUserName(userId);
        return user.getEmail();
    }

    public void validateUserSignUpRequest(SignUpDTO request) {
        if (request.getRole().equals(Role.ADMIN)) {
            throw new IncorrectCredentialsException("Can not Create a user of type admin");
        }
        if (getUserByEmail(request.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Email already exists for a user");
        }
        String password = rsaService.decodeMessage(request.getPassword());
        String confirmPassword = rsaService.decodeMessage(request.getConfirmPassword());
        if (!password.matches(passwordRegEx)) {
            throw new RegexMisMatchException("Please enter a password that is 8 characters long with the other requirements");
        }
        if (!password.equals(confirmPassword)) {
            throw new InvalidUserException("Please make sure that both the passwords are same");
        }

        createUser(request.getEmail(), password, request.getRole(), request.getDob());
    }

    private void createUser(String email, String password, Role role, Date dob) {
        UUID userId = UUID.randomUUID();

        User user = User
                .builder()
                .id(String.valueOf(userId))
                .email(email)
                .password(password)
                .username(String.valueOf(userId))
                .build();

        user.setId(user.getId());
        user.setUsername(user.getUsername());
        UserRepresentation userRepresentation = userToUserRep(user);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).users().create(userRepresentation);
        addRole(userId, role);
        userDetailsService.createUser(email, dob, role, userId);

    }

    public void createAdminUser() {
        if (getUserByEmail(adminEmail) != null) {
            return;
        }
        createUser(adminEmail, adminPassword, Role.ADMIN, new Date());
    }

    public boolean addRole(UUID userId, Role role) throws DetailsNotFoundException {
        User user = getUserByUserName(userId);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        RoleRepresentation roleRepresentation = keycloak.realm(realm).roles().get(String.valueOf(role)).toRepresentation();
        RoleRepresentation removeRoleRepresentation = keycloak.realm(realm).roles().get(defaultRole).toRepresentation();
        keycloak.realm(realm).users().get(user.getId()).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
        keycloak.realm(realm).users().get(user.getId()).roles().realmLevel().remove(Collections.singletonList(removeRoleRepresentation));
        System.out.println("Reached here");
        return true;
    }

    public User updateUser(User user) {
        UserRepresentation userRepresentation = userToUserRep(user);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).users().get(user.getId()).update(userRepresentation);
        return user;
    }

    public void changePassword(String password, User user) {

        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();

        CredentialRepresentation credRepresentation = new CredentialRepresentation();
        credRepresentation.setTemporary(false);
        credRepresentation.setValue(password);

        keycloak.realm(realm).users().get(user.getId()).resetPassword(credRepresentation);

    }

    public void resetPassword(ResetPasswordDTO resetPasswordDTO, String email) {
        String password = rsaService.decodeMessage(resetPasswordDTO.getPassword());
        String confirmPassword = rsaService.decodeMessage(resetPasswordDTO.getConfirmPassword());

        if (!password.matches(passwordRegEx)) {
            throw new RegexMisMatchException("Please enter a password that is 8 characters long with the other requirements");
        }

        if (!password.equals(confirmPassword)) {
            throw new IncorrectCredentialsException("Password and confirm password must be same");
        }
        User user = getUserByEmail(email);
        changePassword(password, user);


    }


    public SignInResponseDTO userLogIn(String email, String password) throws IOException {
        String decodedPassword = rsaService.decodeMessage(password);
        if (!decodedPassword.matches(passwordRegEx)) {
            throw new RegexMisMatchException("Please enter a password that is 8 characters long with the other requirements");
        }

        User userRef = getUserByEmail(email);

        if (userRef == null) {
            throw new IncorrectCredentialsException("Wrong Email");
        }

        String username = userRef.getUsername();

        KeyCloakLoginService keycloakApi = retrofitBuilderConfig.keyCloakRetrofit().create(KeyCloakLoginService.class);
        Call<SignInResponseDTO> call = keycloakApi.getToken(
                "password", clientId, clientSecret, username, decodedPassword, realm);

        Response<SignInResponseDTO> response = call.execute();
        if (!response.isSuccessful()) {
            throw new IncorrectCredentialsException("Wrong email/password");
        }
        if (response.body() == null) {
            throw new ExternalServiceException("Something went wrong");
        }

        SignInResponseDTO tokenResponse = response.body();
        tokenResponse.setAccessToken(tokenResponse.getAccessToken());
        tokenResponse.setRefreshToken(tokenResponse.getRefreshToken());

        if (!email.equals(adminEmail)) {
            tokenResponse.setOnBoardingStatus(userDetailsService.getUserStatus(UUID.fromString(userRef.getUsername())));
        }

        return tokenResponse;

    }

    public SignInResponseDTO regenerateAccessToken(String refreshToken) throws IOException, IncorrectCredentialsException {

        KeyCloakLoginService keycloakApi = retrofitBuilderConfig.keyCloakRetrofit().create(KeyCloakLoginService.class);
        Call<SignInResponseDTO> call = keycloakApi.regenerateToken(
                "refresh_token", clientId, clientSecret, refreshToken, realm);
        Response<SignInResponseDTO> response = call.execute();
        if (!response.isSuccessful()) {
            throw new UnAuthorizedException("Refresh token expired");
        }
        SignInResponseDTO tokenResponse = response.body();
        tokenResponse.setAccessToken(tokenResponse.getAccessToken());
        tokenResponse.setRefreshToken(tokenResponse.getRefreshToken());


        return tokenResponse;

    }

    @Transactional
    public Boolean userLogOut(String refreshToken, UUID userId) throws IOException, DetailsNotFoundException {

        KeyCloakLoginService keycloakApi = retrofitBuilderConfig.keyCloakRetrofit().create(KeyCloakLoginService.class);
        Call<String> call = keycloakApi.logout(clientId, clientSecret, refreshToken, realm);
        Response<String> response = call.execute();

        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        User user = getUserByUserName(userId);

        keycloak.realm(realm).users().get(user.getId()).logout();

        return response.isSuccessful();
    }

    private User userRepToUser(UserRepresentation userRepresentation) {
        return User.builder()
                .id(userRepresentation.getId())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .email(userRepresentation.getEmail())
                .username(userRepresentation.getUsername())
                .build();
    }

    private UserRepresentation userToUserRep(User user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(user.getId());
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        List<CredentialRepresentation> credentialRepresentationList = new ArrayList<>();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(user.getPassword());
        credentialRepresentationList.add(credentialRepresentation);
        userRepresentation.setCredentials(credentialRepresentationList);
        return userRepresentation;
    }
}
