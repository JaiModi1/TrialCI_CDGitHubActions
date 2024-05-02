package com.digitallending.userservice.model.mapper;

import com.digitallending.userservice.model.dto.business.BusinessDocumentDTO;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        BusinessDocumentTypeMapper.class
})
public interface BusinessDocumentMapper {
    BusinessDocumentDTO toResponseDto(MsmeBusinessDocument businessDocument);

    List<BusinessDocumentDTO> toResponseDtoList(List<MsmeBusinessDocument> msmeUserDocumentList);
}
