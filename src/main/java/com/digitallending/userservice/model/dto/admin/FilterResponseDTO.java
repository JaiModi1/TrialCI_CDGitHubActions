package com.digitallending.userservice.model.dto.admin;

import com.digitallending.userservice.model.dto.userregistration.UserDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@Data
public class FilterResponseDTO extends PaginationResponseDto{
    private List<UserDetailsDTO> listOfUsers;
}
