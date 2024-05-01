package com.digitallending.userservice.service.def;

import com.digitallending.userservice.model.dto.business.BusinessDocumentTypeDTO;
import com.digitallending.userservice.model.dto.business.BusinessTypeDTO;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;

import java.util.List;
import java.util.UUID;

public interface BusinessTypeService {
    List<BusinessDocumentTypeDTO> getDocumentTypes(UUID businessTypeId);

    BusinessType getBusinessTypeByBusinessTypeId(UUID businessTypeId);

    boolean isBusinessTypePresent(BusinessType businessType);

    List<BusinessTypeDTO> getAllBusinessTypes();
}
