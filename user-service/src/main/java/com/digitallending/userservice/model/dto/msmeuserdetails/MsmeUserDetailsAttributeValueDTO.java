package com.digitallending.userservice.model.dto.msmeuserdetails;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MsmeUserDetailsAttributeValueDTO {

    private UUID attributeValueId;

    @NotBlank(message = "Please Provide Property Value")
    private String value;
}
