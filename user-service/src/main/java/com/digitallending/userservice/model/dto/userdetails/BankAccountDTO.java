package com.digitallending.userservice.model.dto.userdetails;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankAccountDTO {

    private UUID bankAccountId;

    @NotNull
    @Pattern(regexp = "\\d{11,12}", message = "Please Provide Valid Bank Account Number")
    private String accountNumber;

    @NotNull
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Please Provide Valid IFSC Code")
    private String ifscCode;

    @NotBlank(message = "Please Provide Account Holder Name")
    private String accountHolderName;

    @NotBlank(message = "Please Provide Bank Name")
    private String bankName;
}
