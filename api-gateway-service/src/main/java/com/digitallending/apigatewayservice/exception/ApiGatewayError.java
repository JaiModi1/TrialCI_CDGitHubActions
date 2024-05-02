package com.digitallending.apigatewayservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;


@Data
@Builder
public class ApiGatewayError {
    private Map<String, String> error;
}
