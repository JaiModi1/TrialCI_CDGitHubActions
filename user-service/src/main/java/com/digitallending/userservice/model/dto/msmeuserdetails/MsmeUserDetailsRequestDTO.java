package com.digitallending.userservice.model.dto.msmeuserdetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MsmeUserDetailsRequestDTO {

    private UUID maritalStatus;

    private UUID gender;

    private UUID category;

    private UUID educationalQualification;

}