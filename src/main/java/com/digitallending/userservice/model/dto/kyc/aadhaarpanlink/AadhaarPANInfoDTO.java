package com.digitallending.userservice.model.dto.kyc.aadhaarpanlink;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AadhaarPANInfoDTO {
    private String aadhaarNumber;
    private String pan;
}
