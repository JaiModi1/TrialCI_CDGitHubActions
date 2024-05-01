package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.AttributeValueNotFoundException;
import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.exception.PreviousStepsNotDoneException;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.exception.UserNotFoundException;
import com.digitallending.userservice.model.dto.admin.UserPaginationResponseDTO;
import com.digitallending.userservice.model.dto.business.BusinessDetailsBREDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsAttributeDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsRequestDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsResponseDTO;
import com.digitallending.userservice.model.dto.userdetails.UserBRERequestDTO;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetails;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttribute;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttributeValue;
import com.digitallending.userservice.model.mapper.MsmeUserDetailsAttributeMapper;
import com.digitallending.userservice.model.mapper.MsmeUserDetailsMapper;
import com.digitallending.userservice.repository.MsmeUserDetailsAttributeRepository;
import com.digitallending.userservice.repository.MsmeUserDetailsAttributeValueRepository;
import com.digitallending.userservice.repository.MsmeUserDetailsRepository;
import com.digitallending.userservice.service.def.BusinessDetailsService;
import com.digitallending.userservice.service.def.MsmeUserDetailsService;
import com.digitallending.userservice.service.def.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.digitallending.userservice.utils.DateUtil.getMonthDifference;

@Service
public class MsmeUserDetailsServiceImpl implements MsmeUserDetailsService {

    @Autowired
    private MsmeUserDetailsRepository msmeUserDetailsRepository;

    @Autowired
    private MsmeUserDetailsMapper msmeUserDetailsMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BusinessDetailsService businessDetailsService;

    @Autowired
    private MsmeUserDetailsAttributeRepository msmeUserDetailsAttributeRepository;

    @Autowired
    private MsmeUserDetailsAttributeValueRepository msmeUserDetailsAttributeValueRepository;

    @Autowired
    private MsmeUserDetailsAttributeMapper msmeUserDetailsAttributeMapper;

    @Override
    @Transactional
    public MsmeUserDetailsResponseDTO saveMsmeUserDetails(UUID userId, MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO) {

        UserOnBoardingStatus userStatus = userDetailsService.getUserStatus(userId);
        if (!userStatus.equals(UserOnBoardingStatus.VERIFY_PAN)) {
            throw new PreviousStepsNotDoneException("Please First Verify Pan Number");
        }
        MsmeUserDetails msmeUserDetails = validateMsmeUserDetailsDTOAndConvertToMsmeUserDetails(msmeUserDetailsRequestDTO);
        msmeUserDetails.setUserId(userId);
        MsmeUserDetails savedMsmeUserDetails = msmeUserDetailsRepository.save(msmeUserDetails);
        userDetailsService.updateUserStatus(userId, UserOnBoardingStatus.USER_DETAILS);
        return msmeUserDetailsMapper.toResponseDto(savedMsmeUserDetails);
    }

    @Override
    public MsmeUserDetailsResponseDTO updateMsmeUserDetails(UUID userId, MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO) {

        UserOnBoardingStatus onBoardingStatus = userDetailsService.getUserStatus(userId);
        if (onBoardingStatus.compareTo(UserOnBoardingStatus.USER_DETAILS) < 0 || onBoardingStatus.compareTo(UserOnBoardingStatus.ON_HOLD) > 0) {
            throw new UpdateException("This user can not update details");
        }
        MsmeUserDetails msmeUserDetails = validateMsmeUserDetailsDTOAndConvertToMsmeUserDetails(msmeUserDetailsRequestDTO);
        msmeUserDetails.setUserId(userId);
        MsmeUserDetails savedMsmeUserDetails = msmeUserDetailsRepository.save(msmeUserDetails);
        return msmeUserDetailsMapper.toResponseDto(savedMsmeUserDetails);
    }

    private MsmeUserDetails validateMsmeUserDetailsDTOAndConvertToMsmeUserDetails(MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO) {

        MsmeUserDetailsAttributeValue maritalStatus = msmeUserDetailsAttributeValueRepository
                .findById(msmeUserDetailsRequestDTO.getMaritalStatus())
                .orElseThrow(() -> new AttributeValueNotFoundException("Attribute Value Not Found For Martial Status"));
        if (!maritalStatus.getMsmeUserDetailsAttribute().getAttributeName().equals("maritalStatus")) {
            throw new AttributeValueNotFoundException("For Attribute MartialStatus Provide Valid Value");
        }

        MsmeUserDetailsAttributeValue gender = msmeUserDetailsAttributeValueRepository
                .findById(msmeUserDetailsRequestDTO.getGender())
                .orElseThrow(() -> new AttributeValueNotFoundException("Attribute Value Not Found For Gender"));
        if (!gender.getMsmeUserDetailsAttribute().getAttributeName().equals("gender")) {
            throw new AttributeValueNotFoundException("For Attribute Gender Provide Valid Value");
        }

        MsmeUserDetailsAttributeValue category = msmeUserDetailsAttributeValueRepository
                .findById(msmeUserDetailsRequestDTO.getCategory())
                .orElseThrow(() -> new AttributeValueNotFoundException("Attribute Value Not Found For Category"));
        if (!category.getMsmeUserDetailsAttribute().getAttributeName().equals("category")) {
            throw new AttributeValueNotFoundException("For Attribute Category Provide Valid Value");
        }

        MsmeUserDetailsAttributeValue educationalQualification = msmeUserDetailsAttributeValueRepository
                .findById(msmeUserDetailsRequestDTO.getEducationalQualification())
                .orElseThrow(() -> new AttributeValueNotFoundException("Attribute Value Not Found For Educational Qualification"));
        if (!educationalQualification.getMsmeUserDetailsAttribute().getAttributeName().equals("educationalQualification")) {
            throw new AttributeValueNotFoundException("For Attribute Educational Qualification Provide Valid Value");
        }


        return MsmeUserDetails.builder()
                .maritalStatus(maritalStatus)
                .gender(gender)
                .category(category)
                .educationalQualification(educationalQualification)
                .build();
    }

    @Override
    public MsmeUserDetailsResponseDTO getMsmeUserDetails(UUID userId) {
        MsmeUserDetails msmeUserDetails = msmeUserDetailsRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Exist"));
        return msmeUserDetailsMapper.toResponseDto(msmeUserDetails);
    }

    @Override
    public List<MsmeUserDetailsAttributeDTO> getMsmeUserDetailsAttributeValues() {
        return msmeUserDetailsAttributeRepository
                .findAll()
                .stream()
                .map(msmeUserDetailsAttribute -> msmeUserDetailsAttributeMapper.toResponseDTO(msmeUserDetailsAttribute))
                .toList();
    }

    @Override
    public UserBRERequestDTO getMsmeUserAllDetails(UUID userId) throws DetailsNotFoundException {
        MsmeUserDetails msmeUserDetails = msmeUserDetailsRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Exist"));
        Date dateOfBirth = userDetailsService.getDobByUserId(userId);
        Integer age = getMonthDifference(new Date(), dateOfBirth);
        BusinessDetailsBREDTO businessDetailsBRE = businessDetailsService.getBREValues(userId);
        Integer businessVintage = getMonthDifference(new Date(), businessDetailsBRE.getRegistrationDate());
        Integer businessExperience = getMonthDifference(new Date(), businessDetailsBRE.getBusinessExperience());
        return UserBRERequestDTO.builder().age(age)
                .category(msmeUserDetails.getCategory().getValue())
                .gender(msmeUserDetails.getGender().getValue())
                .maritalStatus(msmeUserDetails.getMaritalStatus().getValue())
                .educationalQualification(msmeUserDetails.getEducationalQualification().getValue())
                .businessVintage(businessVintage)
                .businessExperience(businessExperience)
                .build();
    }

    private UserPaginationResponseDTO pageResponseToUserPaginationResponse(Page<MsmeUserDetails> pageResponse, List<UUID> listOfUserId){
        return UserPaginationResponseDTO
                .builder()
                .listOfUserId(listOfUserId)
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .pageNo(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .isLast(pageResponse.isLast())
                .build();
    }

    @Override
    public UserPaginationResponseDTO listOfUserIdByEducationQualification(String educationQualification , int pageNo, int pageSize) {
        MsmeUserDetailsAttributeValue educationQualificationAttributeValue = msmeUserDetailsAttributeValueRepository.findByValue(educationQualification);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        if (educationQualificationAttributeValue == null) {
            throw new AttributeValueNotFoundException("Please Provide Valid Attribute value");
        }
        Page<MsmeUserDetails> pageResponse = msmeUserDetailsRepository.findByEducationalQualification(educationQualificationAttributeValue, pageable);
        List<MsmeUserDetails> listOfMsmeUserDetails = pageResponse.getContent();
        List<UUID> listOfUserId = new ArrayList<>();
        listOfMsmeUserDetails.forEach(userDetail -> listOfUserId.add(userDetail.getUserId()));
        return pageResponseToUserPaginationResponse(pageResponse, listOfUserId);
    }

    @Override
    public UserPaginationResponseDTO listOfUserIdByCategory(String category, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        MsmeUserDetailsAttributeValue categoryAttributeValue = msmeUserDetailsAttributeValueRepository.findByValue(category);
        Page<MsmeUserDetails> pageResponse = msmeUserDetailsRepository.findByCategory(categoryAttributeValue, pageable);
        List<MsmeUserDetails> listOfMsmeUserDetails = pageResponse.getContent();
        List<UUID> listOfUserId = new ArrayList<>();
        listOfMsmeUserDetails.forEach(userDetail -> listOfUserId.add(userDetail.getUserId()));
        return pageResponseToUserPaginationResponse(pageResponse, listOfUserId);
    }

    @Override
    @Transactional
    public void initializeDatabase() {
        if (0 == msmeUserDetailsAttributeRepository.count()) {
            MsmeUserDetailsAttribute education = msmeUserDetailsAttributeRepository
                    .save(MsmeUserDetailsAttribute
                            .builder()
                            .attributeName("educationalQualification")
                            .build());
            MsmeUserDetailsAttribute gender = msmeUserDetailsAttributeRepository
                    .save(MsmeUserDetailsAttribute
                            .builder()
                            .attributeName("gender")
                            .build());
            MsmeUserDetailsAttribute category = msmeUserDetailsAttributeRepository
                    .save(MsmeUserDetailsAttribute
                            .builder()
                            .attributeName("category")
                            .build());
            MsmeUserDetailsAttribute maritalStatus = msmeUserDetailsAttributeRepository
                    .save(MsmeUserDetailsAttribute
                            .builder()
                            .attributeName("maritalStatus")
                            .build());
            msmeUserDetailsAttributeValueRepository.saveAll(
                    List.of(
                            MsmeUserDetailsAttributeValue.builder().value("MALE").msmeUserDetailsAttribute(gender).build(),
                            MsmeUserDetailsAttributeValue.builder().value("FEMALE").msmeUserDetailsAttribute(gender).build(),
                            MsmeUserDetailsAttributeValue.builder().value("OTHER").msmeUserDetailsAttribute(gender).build(),

                            MsmeUserDetailsAttributeValue.builder().value("OPEN").msmeUserDetailsAttribute(category).build(),
                            MsmeUserDetailsAttributeValue.builder().value("SC_ST").msmeUserDetailsAttribute(category).build(),
                            MsmeUserDetailsAttributeValue.builder().value("OTHERS").msmeUserDetailsAttribute(category).build(),
                            MsmeUserDetailsAttributeValue.builder().value("OBC").msmeUserDetailsAttribute(category).build(),

                            MsmeUserDetailsAttributeValue.builder().value("MARRIED").msmeUserDetailsAttribute(maritalStatus).build(),
                            MsmeUserDetailsAttributeValue.builder().value("UN_MARRIED").msmeUserDetailsAttribute(maritalStatus).build(),
                            MsmeUserDetailsAttributeValue.builder().value("DIVORCED").msmeUserDetailsAttribute(maritalStatus).build(),
                            MsmeUserDetailsAttributeValue.builder().value("WIDOW").msmeUserDetailsAttribute(maritalStatus).build(),

                            MsmeUserDetailsAttributeValue.builder().value("UNDER_10TH").msmeUserDetailsAttribute(education).build(),
                            MsmeUserDetailsAttributeValue.builder().value("SSC_PASS").msmeUserDetailsAttribute(education).build(),
                            MsmeUserDetailsAttributeValue.builder().value("HSC_PASS").msmeUserDetailsAttribute(education).build(),
                            MsmeUserDetailsAttributeValue.builder().value("BACHELORS").msmeUserDetailsAttribute(education).build(),
                            MsmeUserDetailsAttributeValue.builder().value("MASTERS").msmeUserDetailsAttribute(education).build(),
                            MsmeUserDetailsAttributeValue.builder().value("PHD").msmeUserDetailsAttribute(education).build()));

        }

    }

}
