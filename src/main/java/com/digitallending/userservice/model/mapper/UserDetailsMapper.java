package com.digitallending.userservice.model.mapper;

import com.digitallending.userservice.model.dto.userregistration.UserDetailsDTO;
import com.digitallending.userservice.model.dto.userregistration.UserFullProfileDTO;
import com.digitallending.userservice.model.entity.UserDetails;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        BankAccountMapper.class,
        MsmeUserDocumentMapper.class,
        BusinessDocumentMapper.class,

})
public interface UserDetailsMapper {
    UserFullProfileDTO toResponseDTO(UserDetails userDetails);

    UserDetailsDTO toUserDetailsDTO(UserDetails userDetails);

    List<UserDetailsDTO> toListUserDetailsDTO(List<UserDetails> listOfUserDetails);

}
