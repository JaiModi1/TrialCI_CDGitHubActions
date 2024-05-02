package com.digitallending.userservice.model.dto.kyc.aadhaarpanlink;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private String code;
    private String type;
    private String desc;
}
