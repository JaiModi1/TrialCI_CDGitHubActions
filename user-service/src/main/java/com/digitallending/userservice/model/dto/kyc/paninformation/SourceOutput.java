package com.digitallending.userservice.model.dto.kyc.paninformation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SourceOutput {
    private String first_name;
    private String last_name;
    private String middle_name;
    private String name_on_card;
}
