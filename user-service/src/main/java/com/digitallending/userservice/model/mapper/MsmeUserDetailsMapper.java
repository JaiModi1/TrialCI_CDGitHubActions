package com.digitallending.userservice.model.mapper;

import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsResponseDTO;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MsmeUserDetailsAttributeValueMapper.class})
public interface MsmeUserDetailsMapper {

    @Mapping(source ="maritalStatus.value", target = "maritalStatus")
    @Mapping(source ="gender.value", target = "gender")
    @Mapping(source ="category.value", target = "category")
    @Mapping(source ="educationalQualification.value", target = "educationalQualification")
    MsmeUserDetailsResponseDTO toResponseDto(MsmeUserDetails msmeUserDetails);

}
