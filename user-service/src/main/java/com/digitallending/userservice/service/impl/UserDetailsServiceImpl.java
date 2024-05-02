package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.InvalidUserException;
import com.digitallending.userservice.exception.UserNotFoundException;
import com.digitallending.userservice.model.dto.admin.FilterResponseDTO;
import com.digitallending.userservice.model.dto.admin.LenderDTO;
import com.digitallending.userservice.model.dto.admin.LenderListDTO;
import com.digitallending.userservice.model.dto.admin.UserFilterTypesDTO;
import com.digitallending.userservice.model.dto.admin.UserPaginationResponseDTO;
import com.digitallending.userservice.model.dto.business.BusinessDocumentTypeDTO;
import com.digitallending.userservice.model.dto.business.BusinessTypeDTO;
import com.digitallending.userservice.model.dto.userdetails.RoleTypeStatisticsDTO;
import com.digitallending.userservice.model.dto.userregistration.ChangeUserStatusDTO;
import com.digitallending.userservice.model.dto.userregistration.UserDetailsDTO;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttribute;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttributeValue;
import com.digitallending.userservice.model.mapper.UserDetailsMapper;
import com.digitallending.userservice.repository.MsmeUserDetailsAttributeRepository;
import com.digitallending.userservice.repository.UserDetailsRepository;
import com.digitallending.userservice.service.def.BusinessDetailsService;
import com.digitallending.userservice.service.def.BusinessDocumentService;
import com.digitallending.userservice.service.def.BusinessTypeService;
import com.digitallending.userservice.service.def.MsmeUserDetailsService;
import com.digitallending.userservice.service.def.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    @Lazy
    private BusinessDetailsService businessDetailsService;

    @Autowired
    @Lazy
    private BusinessTypeService businessTypeService;

    @Autowired
    @Lazy
    private MsmeUserDetailsService msmeUserDetailsService;

    @Autowired
    private BusinessDocumentService businessDocumentService;

    @Autowired
    private MsmeUserDetailsAttributeRepository msmeUserDetailsAttributeRepository;

    @Override
    public UserDetails createUser(String email, Date dob, Role role, UUID userId) {
        UserDetails user = UserDetails.builder()
                .userId(userId)
                .dob(dob)
                .email(email)
                .role(role)
                .onBoardingStatus(UserOnBoardingStatus.SIGN_UP)
                .build();

        userDetailsRepository.save(user);
        return user;
    }

    @Override
    public String getEmailByUserId(UUID userId) {
        Optional<UserDetails> userDetails = userDetailsRepository.findById(userId);
        if (userDetails.isPresent()) {
            return userDetails.get().getEmail();
        } else {
            throw new UserNotFoundException("Can not find the user");
        }

    }

    @Override
    public void updateEmailByUserId(String email, UUID userId) {
        userDetailsRepository.setEmailByUserId(userId, email);
    }

    public UserDetails updateUserDetails(UserDetails user) {

        return userDetailsRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails getUserDetails(UUID userId) {
        return userDetailsRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }

    @Override
    public boolean adminVerification(ChangeUserStatusDTO request) {
        UserOnBoardingStatus onBoardingStatus = getUserStatus(request.getUserId());
        if (!onBoardingStatus.equals(UserOnBoardingStatus.UNVERIFIED) && !onBoardingStatus.equals(UserOnBoardingStatus.REVERIFY)) {
            throw new InvalidUserException("Can not verify this user");
        }
        if(!request.getOnBoardingStatus().equals(UserOnBoardingStatus.VERIFIED) &&
         !request.getOnBoardingStatus().equals(UserOnBoardingStatus.ON_HOLD)){
            throw new InvalidUserException("Can not update user status");
        }
        UserOnBoardingStatus newStatus = updateUserStatus(request.getUserId(), request.getOnBoardingStatus());
        return newStatus.equals(UserOnBoardingStatus.VERIFIED);

    }

    @Override
    public Date getDobByUserId(UUID userId) {
        UserDetails user = userDetailsRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        return user.getDob();
    }

    @Override
    public void updateDobByUserId(UUID userId, Date dob) {
        UserDetails user = userDetailsRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setDob(dob);
        userDetailsRepository.save(user);
    }

    @Override
    public UserOnBoardingStatus getUserStatus(UUID userId) throws UserNotFoundException {

        UserDetails user = userDetailsRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getOnBoardingStatus();

    }

    @Override
    public UserOnBoardingStatus updateUserStatus(UUID userId, UserOnBoardingStatus onBoardingStatus) {

        userDetailsRepository.setStatusByUserId(userId, String.valueOf(onBoardingStatus));
        return onBoardingStatus;
    }

    @Override
    public FilterResponseDTO getAllUsers(int pageSize, int pageNo) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserDetails> pageResponse = userDetailsRepository.findByRoleNot(Role.ADMIN, pageable);
        List<UserDetails> listOfUsers = pageResponse.getContent();

        return pageResponseToFilterResponse(pageResponse, listOfUsers);
    }

    @Override
    public FilterResponseDTO getAllUserByStatus(UserOnBoardingStatus status, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserDetails> pageResponse = userDetailsRepository.findByOnBoardingStatusAndRoleNot(status, Role.ADMIN, pageable);
        List<UserDetails> listOfUserByStatus = pageResponse.getContent();
        return pageResponseToFilterResponse(pageResponse, listOfUserByStatus);

    }

    @Override
    public FilterResponseDTO getAllUserByRole(Role role, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<UserDetails> pageResponse = userDetailsRepository.findByRole(role, pageable);

        List<UserDetails> listOfUserByRole = pageResponse.getContent();
        return pageResponseToFilterResponse(pageResponse, listOfUserByRole);
    }

    @Override
    public LenderListDTO getAllVerifiedLenders() {
        List<UserDetails> listOfVerifiedLenders = userDetailsRepository.findAllByRoleAndOnBoardingStatus(Role.LENDER, UserOnBoardingStatus.VERIFIED);
        List<LenderDTO> lenders = new ArrayList<>();
        listOfVerifiedLenders.forEach(user -> lenders.add(LenderDTO
                .builder()
                .userId(user.getUserId())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .build()));
        return LenderListDTO.builder()
                .lenderList(lenders)
                .build();
    }

    private FilterResponseDTO pageResponseToFilterResponse(Page<UserDetails> pageResponse, List<UserDetails> listOfUsers) {
        return FilterResponseDTO
                .builder()
                .listOfUsers(userDetailsMapper.toListUserDetailsDTO(listOfUsers))
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .pageNo(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .isLast(pageResponse.isLast())
                .build();
    }

    private FilterResponseDTO userPaginationToFilterResponse(UserPaginationResponseDTO paginationResponse, List<UserDetailsDTO> listOfUsers) {
        return FilterResponseDTO
                .builder()
                .listOfUsers(listOfUsers)
                .totalElements(paginationResponse.getTotalElements())
                .pageNo(paginationResponse.getPageNo())
                .pageSize(paginationResponse.getPageSize())
                .totalPages(paginationResponse.getTotalPages())
                .isLast(paginationResponse.isLast())
                .build();
    }

    @Override
    public FilterResponseDTO getAllUsersByEducation(String educationQualification, int pageNo, int pageSize) {

        UserPaginationResponseDTO userPaginationResponseDTO = msmeUserDetailsService.listOfUserIdByEducationQualification(educationQualification, pageNo, pageSize);
        List<UserDetails> listOfUserByEducation = userDetailsRepository.findAllById(userPaginationResponseDTO.getListOfUserId());

        return userPaginationToFilterResponse(userPaginationResponseDTO, userDetailsMapper.toListUserDetailsDTO(listOfUserByEducation));
    }

    @Override
    public FilterResponseDTO getAllUsersByCategory(String category, int pageNo, int pageSize) {
        UserPaginationResponseDTO userPaginationResponseDTO = msmeUserDetailsService.listOfUserIdByCategory(category, pageNo, pageSize);
        List<UserDetails> listOfUserByCategory = userDetailsRepository.findAllById(userPaginationResponseDTO.getListOfUserId());

        return userPaginationToFilterResponse(userPaginationResponseDTO, userDetailsMapper.toListUserDetailsDTO(listOfUserByCategory));
    }

    @Override
    public FilterResponseDTO getAllUsersByBusiness(String businessType, int pageNo, int pageSize) {
        UserPaginationResponseDTO userPaginationResponseDTO = businessDetailsService.findUserIdByBusinessType(businessType, pageNo, pageSize);
        List<UserDetails> listOfUserByEducation = userDetailsRepository.findAllById(userPaginationResponseDTO.getListOfUserId());

        return userPaginationToFilterResponse(userPaginationResponseDTO, userDetailsMapper.toListUserDetailsDTO(listOfUserByEducation));
    }

    @Override
    public RoleTypeStatisticsDTO countUserByRole() {
        RoleTypeStatisticsDTO roleTypeStatisticsDTO = new RoleTypeStatisticsDTO();
        Map<String, Long> countByRole = new HashMap<>();
        List<Object[]> data = userDetailsRepository.findRoleTypes();
        for (Object[] result : data) {
            String name = (String) result[0];
            Long count = (Long) result[1];
            if (name.equals("MSME")) {
                roleTypeStatisticsDTO.setMsme(count);
            } else {
                roleTypeStatisticsDTO.setLender(count);
            }
            countByRole.put(name, count);
        }
        return roleTypeStatisticsDTO;

    }

    @Override
    public boolean checkUserByUserIdAndRole(UUID userId, Role role) {
        return userDetailsRepository.existsByUserIdAndRole(userId, role);
    }

    @Override
    public UserFilterTypesDTO getFilterTypes() {
        List<String> listOfRoles = new ArrayList<>(Stream.of(Role.values()).map(Enum::toString).toList());

        listOfRoles.remove("ADMIN");

        List<String> listOfUserOnBoardingStatus = Stream.of(UserOnBoardingStatus.values()).map(Enum::toString).toList();


        MsmeUserDetailsAttribute educationalQualification = msmeUserDetailsAttributeRepository.findByAttributeName("educationalQualification");
        List<MsmeUserDetailsAttributeValue> educationalQualificationValues = educationalQualification.getMsmeUserDetailsAttributeValues();

        List<String> listOfEducationQualifications = new ArrayList<>();
        educationalQualificationValues.forEach(value -> listOfEducationQualifications.add(value.getValue()));


        MsmeUserDetailsAttribute category = msmeUserDetailsAttributeRepository.findByAttributeName("category");
        List<MsmeUserDetailsAttributeValue> categoryValues = category.getMsmeUserDetailsAttributeValues();
        List<String> listofCategories = new ArrayList<>();

        categoryValues.forEach(value -> listofCategories.add(value.getValue()));

        List<BusinessTypeDTO> listOfBusinesses = businessTypeService.getAllBusinessTypes();
        List<String> listOfBusinessTypes = new ArrayList<>();
        listOfBusinesses.forEach(business -> listOfBusinessTypes.add(business.getBusinessType()));

        Map<String, List<String>> valueTypes = new HashMap<>();

        valueTypes.put("ROLE", listOfRoles);
        valueTypes.put("STATUS", listOfUserOnBoardingStatus);
        valueTypes.put("BUSINESS", listOfBusinessTypes);
        valueTypes.put("EDUCATION", listOfEducationQualifications);
        valueTypes.put("CATEGORY", listofCategories);


        return UserFilterTypesDTO
                .builder()
                .propertyTypes(Arrays.asList("ALL", "ROLE", "STATUS", "EDUCATION", "CATEGORY", "BUSINESS"))
                .valueTypes(valueTypes)
                .build();
    }

    @Override
    public void applyForVerification(UUID userId, Role role) {
        UserOnBoardingStatus userOnBoardingStatus = getUserStatus(userId);
        if (role.equals(Role.MSME)) {
            List<BusinessDocumentTypeDTO> remainingDocuments = businessDocumentService.getRemainingDocuments(userId, businessDetailsService.getBusinessTypeByUserId(userId).getBusinessTypeId());
            if (remainingDocuments.size() > 1 || (remainingDocuments.size() == 1 && !remainingDocuments.get(0).getBusinessDocumentType().equals("Partner ID Proof"))) {
                throw new InvalidUserException("Can not apply for verification");

            }
        }
        if (userOnBoardingStatus.equals(UserOnBoardingStatus.BANK_DETAILS)) {
            updateUserStatus(userId, UserOnBoardingStatus.UNVERIFIED);
        } else if (userOnBoardingStatus.equals(UserOnBoardingStatus.ON_HOLD)) {
            updateUserStatus(userId, UserOnBoardingStatus.REVERIFY);
        } else {
            throw new InvalidUserException("Can not apply for verification");
        }


    }

}