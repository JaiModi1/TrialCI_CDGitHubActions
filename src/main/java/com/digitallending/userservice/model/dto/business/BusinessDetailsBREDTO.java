package com.digitallending.userservice.model.dto.business;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class BusinessDetailsBREDTO {
    private Date registrationDate;
    private Date businessExperience;
}
