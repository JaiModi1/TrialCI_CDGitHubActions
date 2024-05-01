package com.digitallending.userservice.controller;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.exception.IncorrectCredentialsException;
import com.digitallending.userservice.exception.InvalidUserException;
import com.digitallending.userservice.exception.MailNotSentException;
import com.digitallending.userservice.exception.UserNotFoundException;
import com.digitallending.userservice.model.dto.admin.FilterResponseDTO;
import com.digitallending.userservice.model.dto.admin.LenderListDTO;
import com.digitallending.userservice.model.dto.admin.UserFilterTypesDTO;
import com.digitallending.userservice.model.dto.admin.UserStatisticsDTO;
import com.digitallending.userservice.model.dto.apiresponse.APIResponseDTO;
import com.digitallending.userservice.model.dto.business.BusinessDetailsDTO;
import com.digitallending.userservice.model.dto.business.BusinessDocumentTypeDTO;
import com.digitallending.userservice.model.dto.business.BusinessTypeStatisticsDTO;
import com.digitallending.userservice.model.dto.keycloak.User;
import com.digitallending.userservice.model.dto.mail.MailRequestDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsResponseDTO;
import com.digitallending.userservice.model.dto.userdetails.MsmeUserDetailsDTO;
import com.digitallending.userservice.model.dto.userdetails.RoleTypeStatisticsDTO;
import com.digitallending.userservice.model.dto.userregistration.ChangePasswordDTO;
import com.digitallending.userservice.model.dto.userregistration.ChangeUserStatusDTO;
import com.digitallending.userservice.model.dto.userregistration.ConfirmOTPDTO;
import com.digitallending.userservice.model.dto.userregistration.GenerateEmailOtpDTO;
import com.digitallending.userservice.model.dto.userregistration.ResetPasswordDTO;
import com.digitallending.userservice.model.dto.userregistration.SignInDTO;
import com.digitallending.userservice.model.dto.userregistration.SignInResponseDTO;
import com.digitallending.userservice.model.dto.userregistration.SignUpDTO;
import com.digitallending.userservice.model.dto.userregistration.UpdateDobDTO;
import com.digitallending.userservice.model.dto.userregistration.UserDetailsDTO;
import com.digitallending.userservice.model.dto.userregistration.UserFullProfileDTO;
import com.digitallending.userservice.model.dto.userregistration.UserProfileDTO;
import com.digitallending.userservice.model.dto.userregistration.VerifyEmailOTPDTO;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.mapper.BusinessDetailsMapper;
import com.digitallending.userservice.model.mapper.UserDetailsMapper;
import com.digitallending.userservice.service.def.BusinessDetailsService;
import com.digitallending.userservice.service.def.BusinessTypeService;
import com.digitallending.userservice.service.def.ChangePasswordTokenService;
import com.digitallending.userservice.service.def.EmailOTPService;
import com.digitallending.userservice.service.def.KeyCloakService;
import com.digitallending.userservice.service.def.MsmeUserDetailsService;
import com.digitallending.userservice.service.def.UserDetailsService;
import com.digitallending.userservice.utils.MailServiceUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserDetailsController {

    @Autowired
    private KeyCloakService keyCloakService;

    @Autowired
    private MailServiceUtil mailServiceUtil;

    @Autowired
    private EmailOTPService emailOTPService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ChangePasswordTokenService changePasswordTokenService;

    @Autowired
    private BusinessTypeService businessTypeService;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    private BusinessDetailsService businessDetailsService;

    @Autowired
    @Lazy
    private MsmeUserDetailsService msmeUserDetailsService;

    @Autowired
    private BusinessDetailsMapper businessDetailsMapper;
    /*
     * first time when a new user tries to sign up
     * an entry in keycloak and userDetails is done
     * */
    @PostMapping("/sign-up")
    @Transactional
    public ResponseEntity<APIResponseDTO<String>> signUp(@Valid @RequestBody SignUpDTO request) {
        keyCloakService.validateUserSignUpRequest(request);

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload("User Sign Up Successful")
                .build(), HttpStatus.CREATED);
    }

    /*
     * Receive an access and a refresh token for the creds
     * */
    @PostMapping("/sign-in")
    public ResponseEntity<APIResponseDTO<SignInResponseDTO>> signIn(@Valid @RequestBody SignInDTO signInDTO) throws IOException {
        SignInResponseDTO tokenResponse = keyCloakService.userLogIn(signInDTO.getEmail(), signInDTO.getPassword());

        return new ResponseEntity<>(APIResponseDTO.<SignInResponseDTO>builder()
                .payload(tokenResponse)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/regenerate-token")
    public ResponseEntity<APIResponseDTO<SignInResponseDTO>> regenerateToken(@RequestHeader("RefreshToken") String refreshToken) throws IncorrectCredentialsException, IOException {

        SignInResponseDTO tokenResponse = keyCloakService.regenerateAccessToken(refreshToken);


        return new ResponseEntity<>(APIResponseDTO.<SignInResponseDTO>builder()
                .payload(tokenResponse)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/generate-email-otp")
    public ResponseEntity<APIResponseDTO<String>> generateEmailOTP(@RequestHeader("UserId") UUID userId) throws DetailsNotFoundException, MailNotSentException {

        String email = userDetailsService.getEmailByUserId(userId);

        mailServiceUtil.sendOTPEmail(email);

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload(email)
                .build(), HttpStatus.CREATED);
    }

    /*
     * email is verified and the status of the user is updated
     * */
    @PatchMapping("/verify-email-otp")
    @Transactional
    public ResponseEntity<APIResponseDTO<String>> verifyEmailOTP(@Valid @RequestBody VerifyEmailOTPDTO verifyEmailOTPDTO, @RequestHeader("UserId") UUID userId) throws DetailsNotFoundException {

        String email = userDetailsService.getEmailByUserId(userId);

        emailOTPService.verifyOTP(email, verifyEmailOTPDTO.getOtp());

        userDetailsService.updateUserStatus(userId, UserOnBoardingStatus.VERIFY_EMAIL);

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload("Email verified successfully. Email: " + email)
                .build(), HttpStatus.OK);
    }

    /*
     * User already logged in, and wants to change password
     * */
    @PatchMapping("/change-password")
    public ResponseEntity<APIResponseDTO<String>> changePassword(@Valid @RequestBody ChangePasswordDTO request, @RequestHeader("UserId") UUID userId) throws IOException {

        keyCloakService.initiateChangePassword(request, userId);

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload("Password updated successfully")
                .build(), HttpStatus.OK);
    }

    @PatchMapping("/dob")
    public ResponseEntity<APIResponseDTO<String>> updateDobByUserId(@RequestHeader("UserId") UUID userId, @RequestBody UpdateDobDTO request) {

        userDetailsService.updateDobByUserId(userId, request.getDate());

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload("DOB updated successfully")
                .build(), HttpStatus.OK);
    }

    /*
     * Refresh token is taken out of commission
     * the session of the user is removed from keycloak
     * */
    @GetMapping("/sign-out")
    public ResponseEntity<APIResponseDTO<String>> signOut(@RequestHeader("RefreshToken") String refreshToken, @RequestHeader("UserId") UUID userId) throws IOException, DetailsNotFoundException {

        boolean isLoggedOut = keyCloakService.userLogOut(refreshToken, userId);

        if (isLoggedOut) {
            return new ResponseEntity<>(APIResponseDTO.<String>builder()
                    .payload("Logged out successfully")
                    .build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload("Something went wrong")
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*
     * user forgot password, not logged in
     * */
    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponseDTO<String>> forgotPassword(@Valid @RequestBody GenerateEmailOtpDTO request) throws MailNotSentException {

        keyCloakService.forgotPassword(request);

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload(request.getEmail())
                .build(), HttpStatus.CREATED);
    }

    @PostMapping("/confirm-otp")
    @Transactional
    public ResponseEntity<APIResponseDTO<UUID>> confirmOtp(@Valid @RequestBody ConfirmOTPDTO confirmOTPDTO) {
        emailOTPService.verifyOTP(confirmOTPDTO.getEmail(), confirmOTPDTO.getOtp());

        UUID token = changePasswordTokenService.saveEntry(confirmOTPDTO.getEmail());

        return new ResponseEntity<>(APIResponseDTO.<UUID>builder()
                .payload(token)
                .build(), HttpStatus.OK);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<APIResponseDTO<String>> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) throws DetailsNotFoundException, IncorrectCredentialsException {
        String email = changePasswordTokenService.getEmail(resetPasswordDTO.getToken());

        keyCloakService.resetPassword(resetPasswordDTO, email);

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload("Password changed successfully")
                .build(), HttpStatus.OK);
    }

    /*
     * Get all the details of the user,
     * without the password, which is not exposed by keycloak
     * */
    @GetMapping("/profile")
    public ResponseEntity<APIResponseDTO<UserDetailsDTO>> getUserProfile(@RequestHeader("UserId") UUID userId) throws DetailsNotFoundException {
        UserDetails userDetails = userDetailsService.getUserDetails(userId);

        //changes here

        return new ResponseEntity<>(APIResponseDTO.<UserDetailsDTO>builder()
                .payload(userDetailsMapper.toUserDetailsDTO(userDetails))
                .build(), HttpStatus.OK);
    }

    @GetMapping("/detail-profile")
    @Transactional
    public ResponseEntity<APIResponseDTO<UserFullProfileDTO>> getDetailProfile(@RequestHeader("UserId") UUID userId) throws DetailsNotFoundException {
        UserDetails user = userDetailsService.getUserDetails(userId);

        UserFullProfileDTO longUserDetails = userDetailsMapper.toResponseDTO(user);
        MsmeUserDetailsResponseDTO msmeUserDetails;
        BusinessDetailsDTO msmeBusinssDetails;
        try{
            msmeUserDetails = msmeUserDetailsService.getMsmeUserDetails(userId);
        }
        catch (Exception err){
            msmeUserDetails = null;
        }
        try{
            msmeBusinssDetails = businessDetailsMapper.toResponseDTO(businessDetailsService.getBusinessDetails(userId));
        } catch (Exception err){
            msmeBusinssDetails = null;
        }
        longUserDetails.setMsmeUserDetails(msmeUserDetails);
        longUserDetails.setMsmeBusinessDetails(msmeBusinssDetails);
        return new ResponseEntity<>(APIResponseDTO.<UserFullProfileDTO>builder()
                .payload(longUserDetails)
                .build(), HttpStatus.OK);
    }


    @PostMapping("/initiate-update-email")
    public ResponseEntity<APIResponseDTO<String>> generateOtpToUpdateEmail(@Valid @RequestBody GenerateEmailOtpDTO request) throws MailNotSentException {

        keyCloakService.initiateUpdateEmail(request);

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload(request.getEmail())
                .build(), HttpStatus.CREATED);
    }

    /*
     * This endpoint is used to verify a new email
     * and update it, user is logged in here
     * */
    @PatchMapping("/update-email")
    @Transactional
    public ResponseEntity<APIResponseDTO<String>> verifyEmailOTPInProfile(@Valid @RequestBody ConfirmOTPDTO request, @RequestHeader("UserId") UUID userId) throws DetailsNotFoundException {

        emailOTPService.verifyOTP(request.getEmail(), request.getOtp());

        if (userDetailsService.getUserStatus(userId).equals(UserOnBoardingStatus.SIGN_UP)) {
            userDetailsService.updateUserStatus(userId, UserOnBoardingStatus.VERIFY_EMAIL);
        }
        User user = keyCloakService.getUserByUserName(userId);
        user.setEmail(request.getEmail());
        userDetailsService.updateEmailByUserId(request.getEmail(), userId);
        keyCloakService.updateUser(user);

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload("OTP Verified Successfully")
                .build(), HttpStatus.OK);
    }

    @PatchMapping("/apply")
    @Transactional
    public ResponseEntity<APIResponseDTO<String>> applyForVerification(@RequestHeader("UserId") UUID userId, @RequestHeader("Role") Role role) {
        userDetailsService.applyForVerification(userId, role);

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload("Applied for verification successfully")
                .build(), HttpStatus.OK);
    }


    /*
     * Admin endpoints
     * */
    @PatchMapping("/change-status")
    public ResponseEntity<APIResponseDTO<String>> changeUserStatusByUserId(@RequestBody ChangeUserStatusDTO request) throws UserNotFoundException, DetailsNotFoundException, MailNotSentException {

        User user = keyCloakService.getUserByUserName(request.getUserId());
        String status = "REJECTED";

        if (userDetailsService.adminVerification(request)) {
            status = "VERIFIED";
        }
        mailServiceUtil.sendStatusMail(user.getEmail(), status, request.getStatement());
        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload("User " + status)
                .build(), HttpStatus.OK);

    }


    @GetMapping("/full-profile")
    @Transactional
    public ResponseEntity<APIResponseDTO<UserFullProfileDTO>> getVerboseUserDetails(@RequestParam("userId") UUID userId) throws DetailsNotFoundException {
        UserDetails user = userDetailsService.getUserDetails(userId);

        UserFullProfileDTO longUserDetails = userDetailsMapper.toResponseDTO(user);
        MsmeUserDetailsResponseDTO msmeUserDetails;
        BusinessDetailsDTO msmeBusinssDetails;
        try{
            msmeUserDetails = msmeUserDetailsService.getMsmeUserDetails(userId);
        }
        catch (Exception err){
            msmeUserDetails = null;
        }
        try{
            msmeBusinssDetails = businessDetailsMapper.toResponseDTO(businessDetailsService.getBusinessDetails(userId));
        } catch (Exception err){
            msmeBusinssDetails = null;
        }
        longUserDetails.setMsmeUserDetails(msmeUserDetails);
        longUserDetails.setMsmeBusinessDetails(msmeBusinssDetails);
        return new ResponseEntity<>(APIResponseDTO.<UserFullProfileDTO>builder()
                .payload(longUserDetails)
                .build(), HttpStatus.OK);
    }

    /*
     * role, status, category, education qualification, businesstype
     * */
    @GetMapping("/filter-types")
    public ResponseEntity<APIResponseDTO<UserFilterTypesDTO>> getFilterTypes() {
        return new ResponseEntity<>(APIResponseDTO
                .<UserFilterTypesDTO>builder()
                .payload(userDetailsService.getFilterTypes())
                .build(), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<APIResponseDTO<FilterResponseDTO>> getFilteredUsers(@RequestParam("property") String property,
                                                                              @RequestParam(value = "value", required = false) String value,
                                                                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                              @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
        FilterResponseDTO filterResponse;

        switch (property) {
            case "ALL" -> filterResponse = userDetailsService.getAllUsers(pageSize, pageNo);
            case "ROLE" -> filterResponse = userDetailsService
                    .getAllUserByRole(Role.valueOf(value), pageNo, pageSize);
            case "STATUS" -> filterResponse = userDetailsService
                    .getAllUserByStatus(UserOnBoardingStatus.valueOf(value), pageNo, pageSize);
            case "EDUCATION" -> filterResponse = userDetailsService
                    .getAllUsersByEducation(value, pageNo, pageSize);
            case "CATEGORY" -> filterResponse = userDetailsService
                    .getAllUsersByCategory(value, pageNo, pageSize);
            case "BUSINESS" -> filterResponse = userDetailsService
                    .getAllUsersByBusiness(value, pageNo, pageSize);
            default -> filterResponse = null;
        }

        return new ResponseEntity<>(APIResponseDTO.<FilterResponseDTO>builder()
                .payload(filterResponse)
                .build(), HttpStatus.OK);

    }

    @GetMapping("/lender")
    public ResponseEntity<APIResponseDTO<LenderListDTO>> getLenderList(){


        LenderListDTO listOfLenders = userDetailsService.getAllVerifiedLenders();
        return new ResponseEntity<>(APIResponseDTO.<LenderListDTO>builder()
                .payload(listOfLenders)
                    .build(), HttpStatus.OK);

    }

    @GetMapping("/admin-stats")
    public ResponseEntity<APIResponseDTO<UserStatisticsDTO>> getChartDetails() {

        RoleTypeStatisticsDTO statisticsByRole = userDetailsService.countUserByRole();

        BusinessTypeStatisticsDTO statisticsByBusinessType = businessDetailsService.countByBusinessType();

        return new ResponseEntity<>(APIResponseDTO.<UserStatisticsDTO>builder()
                .payload(UserStatisticsDTO
                        .builder()
                        .role(statisticsByRole)
                        .business(statisticsByBusinessType)
                        .build())
                .build(), HttpStatus.OK);

    }

    /*
     * Internal Calls
     * */

    @GetMapping("{userId}/role")
    ResponseEntity<APIResponseDTO<Role>> getUserRole(@PathVariable("userId") UUID userId) {
        UserDetails userDetails = userDetailsService.getUserDetails(userId);

        return new ResponseEntity<>(APIResponseDTO.<Role>builder()
                .payload(userDetails.getRole())
                .build(), HttpStatus.OK);
    }

    @GetMapping("{userId}/name")
    ResponseEntity<APIResponseDTO<String>> getUserName(@PathVariable("userId") UUID userId) {
        UserDetails userDetails = userDetailsService.getUserDetails(userId);
        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload(userDetails.getFirstName() + " " + userDetails.getLastName())
                .build(), HttpStatus.OK);
    }

    @GetMapping("/check-user")
    ResponseEntity<APIResponseDTO<Boolean>> checkUsers(@RequestParam("userId") UUID userId, @RequestParam("role") Role role) {

        return new ResponseEntity<>(APIResponseDTO.<Boolean>builder()
                .payload(userDetailsService.checkUserByUserIdAndRole(userId, role))
                .build(), HttpStatus.OK);
    }

    @PostMapping("/send-mail")
    ResponseEntity<APIResponseDTO<Boolean>> sendMail(@RequestBody MailRequestDTO mailRequestDTO) {

        User userDetails = keyCloakService.getUserByUserName(mailRequestDTO.getUserId());
        mailServiceUtil.sendMail(userDetails.getEmail(), mailRequestDTO.getSubject(), mailRequestDTO.getMessage());
        return new ResponseEntity<>(APIResponseDTO.
                <Boolean>builder()
                .payload(true)
                .build(), HttpStatus.OK);
    }

}
