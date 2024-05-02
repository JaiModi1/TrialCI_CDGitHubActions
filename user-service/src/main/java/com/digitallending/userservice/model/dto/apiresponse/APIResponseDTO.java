package com.digitallending.userservice.model.dto.apiresponse;

import lombok.Builder;
import lombok.Data;

import java.time.ZoneId;
import java.time.ZonedDateTime;


@Data
@Builder
public class APIResponseDTO<T> {
    private T payload;
    @Builder.Default
    private ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
}
