package com.digitallending.userservice.model.dto.kyc.paninformation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PANInformationResponseDTO {
    private String status;
    private Result result;
    private String message;
}
