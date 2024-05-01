package com.digitallending.userservice.model.dto.kyc.paninformation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PANDTO {

    @NotBlank(message = "Please Provide PanNo")
    private String panNo;
}
