package com.digitallending.userservice.model.dto.admin;

import com.digitallending.userservice.model.dto.business.BusinessTypeStatisticsDTO;
import com.digitallending.userservice.model.dto.userdetails.RoleTypeStatisticsDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatisticsDTO {
    private RoleTypeStatisticsDTO role;
    private BusinessTypeStatisticsDTO business;
}
