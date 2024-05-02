package com.digitallending.userservice.model.dto.kyc.aadhaarpanlink;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AadhaarPANLinkResponseDTO {

    private ArrayList<MessageDTO> messages;

}
