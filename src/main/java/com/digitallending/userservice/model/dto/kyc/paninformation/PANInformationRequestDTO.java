package com.digitallending.userservice.model.dto.kyc.paninformation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PANInformationRequestDTO {

    public String task_id;
    public String group_id;

    public PANRequestDTO data;
}
