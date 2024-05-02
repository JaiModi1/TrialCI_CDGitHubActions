package com.digitallending.userservice.model.mapper;

import com.digitallending.userservice.model.dto.business.BusinessTypeDTO;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusinessTypeMapper {
    BusinessTypeDTO toResponseDTO(BusinessType businessType);

    List<BusinessTypeDTO> toListResponse(List<BusinessType> listOfBusinessTypes);
}
