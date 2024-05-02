package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.BusinessTypeNotExistsException;
import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.exception.PreviousStepsNotDoneException;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.model.dto.admin.UserPaginationResponseDTO;
import com.digitallending.userservice.model.dto.business.BusinessDetailsBREDTO;
import com.digitallending.userservice.model.dto.business.BusinessTypeStatisticsDTO;
import com.digitallending.userservice.model.dto.business.SaveBusinessDetailsDTO;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDetails;
import com.digitallending.userservice.model.mapper.BusinessDetailsMapper;
import com.digitallending.userservice.repository.BusinessDetailsRepository;
import com.digitallending.userservice.service.def.BusinessDetailsService;
import com.digitallending.userservice.service.def.BusinessDocumentService;
import com.digitallending.userservice.service.def.BusinessTypeService;
import com.digitallending.userservice.service.def.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BusinessDetailsServiceImpl implements BusinessDetailsService {
    @Autowired
    private BusinessDetailsRepository businessDetailsRepo;

    @Autowired
    private BusinessTypeService businessTypeService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BusinessDocumentService businessDocumentService;

    @Autowired
    private BusinessDetailsMapper businessDetailsMapper;

    @Override
    @Transactional
    public String saveBusinessDetails(SaveBusinessDetailsDTO businessDetailsDTO, UUID userId) {
        BusinessType businessType = businessTypeService.getBusinessTypeByBusinessTypeId(businessDetailsDTO.getBusinessTypeId());
        if (!businessTypeService.isBusinessTypePresent(businessType)) {
            throw new BusinessTypeNotExistsException("This business type is not valid");
        }

        UserOnBoardingStatus userStatus = userDetailsService.getUserStatus(userId);
        if (!userStatus.equals(UserOnBoardingStatus.USER_DOC)) {
            throw new PreviousStepsNotDoneException("Please submit user documents first");
        }


        userDetailsService.updateUserStatus(userId, UserOnBoardingStatus.BUSINESS_DETAILS);
        businessDetailsRepo.save(MsmeBusinessDetails
                .builder()
                .businessType(businessType)
                .companyName(businessDetailsDTO.getCompanyName())
                .registrationDate(businessDetailsDTO.getRegistrationDate())
                .companyPan(businessDetailsDTO.getCompanyPan())
                .userId(userId)
                .businessExperience(businessDetailsDTO.getBusinessExperience())
                .build());

        return "Business details saved successfully";
    }

    @Override
    @Transactional
    public String updateBusinessDetails(SaveBusinessDetailsDTO businessDetailsDTO, UUID userId) {

        UserOnBoardingStatus onBoardingStatus = userDetailsService.getUserStatus(userId);
        BusinessType newBusinessType = businessTypeService.getBusinessTypeByBusinessTypeId(businessDetailsDTO.getBusinessTypeId());
        if (onBoardingStatus.compareTo(UserOnBoardingStatus.BUSINESS_DETAILS) < 0 || onBoardingStatus.compareTo(UserOnBoardingStatus.ON_HOLD) > 0) {
            throw new UpdateException("This user can not update details");
        }
        if (!businessTypeService.isBusinessTypePresent(newBusinessType)) {
            throw new BusinessTypeNotExistsException("This business type is not valid");
        }

        BusinessType businessType = getBusinessDetails(userId).getBusinessType();
        if (!businessDetailsDTO.getBusinessTypeId().equals(businessType.getBusinessTypeId())) {
            businessDocumentService.changeBusinessType(businessType, newBusinessType, userId);
        }

        businessDetailsRepo.save(MsmeBusinessDetails
                .builder()
                .businessType(newBusinessType)
                .companyName(businessDetailsDTO.getCompanyName())
                .registrationDate(businessDetailsDTO.getRegistrationDate())
                .companyPan(businessDetailsDTO.getCompanyPan())
                .businessExperience(businessDetailsDTO.getBusinessExperience())
                .userId(userId)
                .build());
        return "Updated successfully";
    }

    @Override
    public MsmeBusinessDetails getBusinessDetails(UUID userId) throws DetailsNotFoundException {
        return businessDetailsRepo.findById(userId).orElseThrow(() -> new DetailsNotFoundException("Business Details not found for the user"));
    }

    @Override
    public BusinessDetailsBREDTO getBREValues(UUID userId) throws DetailsNotFoundException {
        MsmeBusinessDetails businessDetails = businessDetailsRepo.findById(userId).orElseThrow(() -> new DetailsNotFoundException("Business Details not found for the user"));
        return BusinessDetailsBREDTO
                .builder()
                .businessExperience(businessDetails.getBusinessExperience())
                .registrationDate(businessDetails.getRegistrationDate())
                .build();
    }

    @Override
    public BusinessTypeStatisticsDTO countByBusinessType() {
        List<Object[]> data = businessDetailsRepo.findBusinessTypes();
        Map<String, Long> countByBusinessType = new HashMap<>();

        for (Object[] result : data) {
            UUID name = (UUID) result[0];
            Long count = (Long) result[1];
            countByBusinessType.put(businessTypeService.getBusinessTypeByBusinessTypeId(name).getBusinessType(), count);
        }
        return BusinessTypeStatisticsDTO
                .builder()
                .partnership(countByBusinessType.getOrDefault("Partnership", 0L))
                .privatLC(countByBusinessType.getOrDefault("Private Limited Company", 0L))
                .publicLC(countByBusinessType.getOrDefault("Public Limited Company", 0L))
                .solePropritership(countByBusinessType.getOrDefault("Sole Proprietorship", 0L))
                .build();
    }

    @Override
    public UserPaginationResponseDTO findUserIdByBusinessType(String businessType, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<MsmeBusinessDetails> pageResponse = businessDetailsRepo.findByBusinessTypeBusinessType(businessType, pageable);
        List<MsmeBusinessDetails> listOfBusinessDetails = pageResponse.getContent();
        List<UUID> listOfUserId = new ArrayList<>();
        listOfBusinessDetails.forEach(businessDetail -> listOfUserId.add(businessDetail.getUserId()));
        return UserPaginationResponseDTO
                .builder()
                .listOfUserId(listOfUserId)
                .isLast(pageResponse.isLast())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .pageNo(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .build();
    }

    @Override
    public BusinessType getBusinessTypeByUserId(UUID userId) {
        MsmeBusinessDetails businessDetails = getBusinessDetails(userId);
        return businessDetails.getBusinessType();
    }
}
