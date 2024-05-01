package com.digitallending.userservice.model.mapper;

import com.digitallending.userservice.model.dto.business.BusinessDetailsDTO;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        BusinessTypeMapper.class
})
public interface BusinessDetailsMapper {
    BusinessDetailsDTO toResponseDTO(MsmeBusinessDetails businessDetails);
}
