package com.digitallending.userservice.model.dto.kyc.aadharotp;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AadhaarDTO {

    @NotBlank(message = "Please Provide Aadhaar No")
    private String aadhaar;
}
