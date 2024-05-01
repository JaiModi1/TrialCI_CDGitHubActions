package com.digitallending.userservice.model.dto.kyc.mobileotp;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileNoDTO {
    @NotNull
    @Pattern(regexp = "^\\+91[1-9][0-9]{9}$", message = "Please Provide Valid Mobile Number")
    private String mobileNo;

}
