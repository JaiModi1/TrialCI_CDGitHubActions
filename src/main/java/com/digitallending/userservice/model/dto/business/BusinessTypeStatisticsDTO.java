package com.digitallending.userservice.model.dto.business;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BusinessTypeStatisticsDTO {
    private long solePropritership;
    private long partnership;
    private long publicLC;
    private long privatLC;
}
