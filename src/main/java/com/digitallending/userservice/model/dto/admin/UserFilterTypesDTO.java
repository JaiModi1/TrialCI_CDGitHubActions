package com.digitallending.userservice.model.dto.admin;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class UserFilterTypesDTO {
    private List<String> propertyTypes;
    private Map<String, List<String>> valueTypes;
}
