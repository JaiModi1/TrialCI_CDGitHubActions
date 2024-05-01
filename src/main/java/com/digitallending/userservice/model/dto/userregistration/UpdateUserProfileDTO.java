package com.digitallending.userservice.model.dto.userregistration;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import lombok.Data;

import java.util.Date;


@Data
public class UpdateUserProfileDTO {
    private String firstName;
    private String lastName;
    private Long phoneNo;
    private Date dob;
    private UserOnBoardingStatus onBoardingStatus;
    private Role role;
}
