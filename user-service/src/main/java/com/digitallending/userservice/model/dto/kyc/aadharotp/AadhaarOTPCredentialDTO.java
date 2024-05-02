package com.digitallending.userservice.model.dto.kyc.aadharotp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AadhaarOTPCredentialDTO {

    private String clientId;
    private String clientSecret;

}
