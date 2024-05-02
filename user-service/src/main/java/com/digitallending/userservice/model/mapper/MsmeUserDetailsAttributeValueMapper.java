package com.digitallending.userservice.model.mapper;

import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsAttributeValueDTO;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttributeValue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MsmeUserDetailsAttributeValueMapper {
       MsmeUserDetailsAttributeValueDTO toResponseDTO(MsmeUserDetailsAttributeValue msmeUserDetailsAttributeValue);
}
