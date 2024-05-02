package com.digitallending.userservice.service.def;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.exception.IncorrectCredentialsException;
import com.digitallending.userservice.exception.MailNotSentException;
import com.digitallending.userservice.model.dto.keycloak.User;
import com.digitallending.userservice.model.dto.userregistration.ChangePasswordDTO;
import com.digitallending.userservice.model.dto.userregistration.GenerateEmailOtpDTO;
import com.digitallending.userservice.model.dto.userregistration.ResetPasswordDTO;
import com.digitallending.userservice.model.dto.userregistration.SignInResponseDTO;
import com.digitallending.userservice.model.dto.userregistration.SignUpDTO;

import java.io.IOException;
import java.util.UUID;

public interface KeyCloakService {

    User getUserByEmail(String emailId);

    boolean checkUserByEmail(String emailId);

    void initiateUpdateEmail(GenerateEmailOtpDTO request) throws MailNotSentException;

    void forgotPassword(GenerateEmailOtpDTO request) throws MailNotSentException;

    User getUserByUserName(UUID userId) throws DetailsNotFoundException;

    void initiateChangePassword(ChangePasswordDTO request, UUID userId) throws IOException;

    String getEmailByUserName(UUID userId) throws DetailsNotFoundException;

    void validateUserSignUpRequest(SignUpDTO request);

    void createAdminUser();

    boolean addRole(UUID userId, Role role) throws DetailsNotFoundException;

    User updateUser(User user);

    void changePassword(String password, User user);

    void resetPassword(ResetPasswordDTO resetPasswordDTO, String email);

    SignInResponseDTO userLogIn(String email, String password) throws IOException;

    Boolean userLogOut(String refreshToken, UUID userId) throws IOException, DetailsNotFoundException;

    SignInResponseDTO regenerateAccessToken(String refreshToken) throws IOException, IncorrectCredentialsException;
}
