package com.digitallending.userservice.service.def;


import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.model.dto.admin.UserPaginationResponseDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsAttributeDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsRequestDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsResponseDTO;
import com.digitallending.userservice.model.dto.userdetails.UserBRERequestDTO;

import java.util.List;
import java.util.UUID;

public interface MsmeUserDetailsService {

    MsmeUserDetailsResponseDTO saveMsmeUserDetails(UUID userId, MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO);

    MsmeUserDetailsResponseDTO updateMsmeUserDetails(UUID userId, MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO);

    MsmeUserDetailsResponseDTO getMsmeUserDetails(UUID userId);

    UserBRERequestDTO getMsmeUserAllDetails(UUID userId) throws DetailsNotFoundException;

    UserPaginationResponseDTO listOfUserIdByEducationQualification(String educationQualification, int pageNo, int pageSize);

    void initializeDatabase();

    UserPaginationResponseDTO listOfUserIdByCategory(String category, int pageNo, int pageSize);

    List<MsmeUserDetailsAttributeDTO> getMsmeUserDetailsAttributeValues();

}
