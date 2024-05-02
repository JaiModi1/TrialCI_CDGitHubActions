package com.digitallending.userservice.model.mapper;

import com.digitallending.userservice.model.dto.userdetails.MsmeUserDocumentDTO;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MsmeUserDocumentMapper {

    MsmeUserDocumentDTO toResponseDto(MsmeUserDocument msmeUserDocument);

    List<MsmeUserDocumentDTO> toResponseDtoList(List<MsmeUserDocument> msmeUserDocumentList);


}
