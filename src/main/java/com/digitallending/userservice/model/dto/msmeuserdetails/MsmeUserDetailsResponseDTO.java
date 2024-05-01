package com.digitallending.userservice.model.dto.msmeuserdetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MsmeUserDetailsResponseDTO {

    private String maritalStatus;

    private String gender;

    private String category;

    private String educationalQualification;

}
