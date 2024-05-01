package com.digitallending.userservice.model.mapper;

import com.digitallending.userservice.model.dto.business.BusinessDocumentTypeDTO;
import com.digitallending.userservice.model.entity.BusinessDocumentType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessDocumentTypeMapper {
    BusinessDocumentTypeDTO toResponseDTO(BusinessDocumentType businessDocumentType);
}
