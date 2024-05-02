package com.digitallending.userservice.model.dto.userdetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleTypeStatisticsDTO {
    private long msme;
    private long lender;
}
