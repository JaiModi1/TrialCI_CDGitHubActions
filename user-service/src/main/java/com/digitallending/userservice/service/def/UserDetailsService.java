package com.digitallending.userservice.service.def;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.UserNotFoundException;
import com.digitallending.userservice.model.dto.admin.FilterResponseDTO;
import com.digitallending.userservice.model.dto.admin.LenderListDTO;
import com.digitallending.userservice.model.dto.admin.UserFilterTypesDTO;
import com.digitallending.userservice.model.dto.userdetails.RoleTypeStatisticsDTO;
import com.digitallending.userservice.model.dto.userregistration.ChangeUserStatusDTO;
import com.digitallending.userservice.model.dto.userregistration.UserDetailsDTO;
import com.digitallending.userservice.model.entity.UserDetails;

import java.util.Date;
import java.util.UUID;

public interface UserDetailsService {

    UserDetails createUser(String email, Date dob, Role role, UUID userId);

    String getEmailByUserId(UUID userId);

    void updateEmailByUserId(String email, UUID userId);

    FilterResponseDTO getAllUsers(int pageSize, int pageNo);

    UserDetails getUserDetails(UUID userId);

    boolean adminVerification(ChangeUserStatusDTO request);

    UserOnBoardingStatus getUserStatus(UUID userId) throws UserNotFoundException;

    UserOnBoardingStatus updateUserStatus(UUID userId, UserOnBoardingStatus status);

    UserDetails updateUserDetails(UserDetails user);

    Date getDobByUserId(UUID userId);

    void updateDobByUserId(UUID userId, Date dob);

    FilterResponseDTO getAllUserByStatus(UserOnBoardingStatus status, int pageNo, int pageSize);

    FilterResponseDTO getAllUserByRole(Role role, int pageNo, int pageSize);

    LenderListDTO getAllVerifiedLenders();


    FilterResponseDTO getAllUsersByEducation(String educationQualification, int pageNo, int pageSize);

    FilterResponseDTO getAllUsersByCategory(String category, int pageNo, int PageSize);

    FilterResponseDTO getAllUsersByBusiness(String businessType, int pageNo, int pageSize);

    RoleTypeStatisticsDTO countUserByRole();

    boolean checkUserByUserIdAndRole(UUID userId, Role role);

    UserFilterTypesDTO getFilterTypes();

    void applyForVerification(UUID userId, Role role);
}
