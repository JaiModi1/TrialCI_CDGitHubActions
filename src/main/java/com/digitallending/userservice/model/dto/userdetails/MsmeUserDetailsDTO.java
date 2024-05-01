package com.digitallending.userservice.model.dto.userdetails;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MsmeUserDetailsDTO {

    @NotBlank(message = "Please Provide Martial Status value")
    private String maritalStatus;

    @NotBlank(message = "Please Provide Gender value")
    private String gender;

    @NotBlank(message = "Please Provide Category value")
    private String category;

    @NotBlank(message = "Please Provide Education Qualification value")
    private String educationQualification;

}
