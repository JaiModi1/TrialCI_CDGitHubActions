package com.digitallending.apigatewayservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Builder
@Data
public class ApiResponse<T> {
    private T payload;
    @Builder.Default
    private ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
}