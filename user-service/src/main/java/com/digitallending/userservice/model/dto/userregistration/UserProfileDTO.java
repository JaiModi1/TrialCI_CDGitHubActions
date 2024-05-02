package com.digitallending.userservice.model.dto.userregistration;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Builder
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private Long phoneNo;
    private String emailId;
    private Date dob;
    private Role role;
    private UserOnBoardingStatus onBoardingStatus;
}
