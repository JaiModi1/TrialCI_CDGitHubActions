package com.digitallending.userservice.model.dto.userregistration;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserStatusDTO {
    @NotBlank
    private UUID userId;
    @NotBlank
    private UserOnBoardingStatus onBoardingStatus;
    @NotBlank
    private String statement;
}
