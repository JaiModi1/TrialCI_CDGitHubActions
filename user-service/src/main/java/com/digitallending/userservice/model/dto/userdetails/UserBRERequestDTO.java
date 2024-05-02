package com.digitallending.userservice.model.dto.userdetails;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserBRERequestDTO {
    private Integer age;
    private String gender;
    private String educationalQualification;
    private String maritalStatus;
    private String category;
    private Integer businessVintage;
    private Integer businessExperience;
}
