package com.digitallending.userservice.model.mapper;

import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsAttributeDTO;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttribute;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = MsmeUserDetailsAttributeValueMapper.class)
public interface MsmeUserDetailsAttributeMapper {

     MsmeUserDetailsAttributeDTO toResponseDTO(MsmeUserDetailsAttribute msmeUserDetailsAttribute);
}
