package com.digitallending.userservice.model.dto.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveBusinessDetailsDTO {
    @NotBlank(message = "Please enter the company name")
    @Pattern(regexp = "^[A-Z][A-Za-z0-9-.& ]{1,79}$", message = "Please enter a valid company name")
    private String companyName;

    @NotBlank(message = "Please enter company PAN number")
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Enter a valid PAN number")
    private String companyPan;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    private Date registrationDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    private Date businessExperience;

    private UUID businessTypeId;
}
