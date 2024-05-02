package com.digitallending.userservice.model.dto.admin;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class LenderListDTO {
    List<LenderDTO> lenderList;
}
