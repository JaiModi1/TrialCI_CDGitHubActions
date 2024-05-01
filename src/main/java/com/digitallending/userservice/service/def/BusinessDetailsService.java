package com.digitallending.userservice.service.def;

import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.model.dto.admin.UserPaginationResponseDTO;
import com.digitallending.userservice.model.dto.business.BusinessDetailsBREDTO;
import com.digitallending.userservice.model.dto.business.BusinessDetailsDTO;
import com.digitallending.userservice.model.dto.business.BusinessTypeStatisticsDTO;
import com.digitallending.userservice.model.dto.business.SaveBusinessDetailsDTO;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDetails;

import java.util.UUID;

public interface BusinessDetailsService {
    String updateBusinessDetails(SaveBusinessDetailsDTO businessDetailsDTO, UUID userId);

    String saveBusinessDetails(SaveBusinessDetailsDTO businessDetailsDTO, UUID userId);

    MsmeBusinessDetails getBusinessDetails(UUID userId) throws DetailsNotFoundException;

    BusinessType getBusinessTypeByUserId(UUID userId) throws DetailsNotFoundException;

    BusinessDetailsBREDTO getBREValues(UUID userId) throws DetailsNotFoundException;

    BusinessTypeStatisticsDTO countByBusinessType();

    UserPaginationResponseDTO findUserIdByBusinessType(String businessType, int pageNo, int pageSize);
}
