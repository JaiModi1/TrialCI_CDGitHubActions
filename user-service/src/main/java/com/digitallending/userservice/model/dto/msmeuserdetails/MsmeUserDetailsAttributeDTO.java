package com.digitallending.userservice.model.dto.msmeuserdetails;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
@Builder
public class MsmeUserDetailsAttributeDTO {

    private UUID attributeId;

    private String attributeName;

    private List<MsmeUserDetailsAttributeValueDTO> msmeUserDetailsAttributeValues;
}
