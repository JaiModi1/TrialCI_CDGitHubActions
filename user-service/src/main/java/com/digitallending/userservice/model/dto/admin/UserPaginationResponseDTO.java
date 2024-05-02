package com.digitallending.userservice.model.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;


@SuperBuilder
@Data
public class UserPaginationResponseDTO extends PaginationResponseDto{
    private List<UUID> listOfUserId;
}
