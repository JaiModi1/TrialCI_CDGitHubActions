package com.digitallending.userservice.service;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.InvalidUserException;
import com.digitallending.userservice.exception.UserNotFoundException;
import com.digitallending.userservice.model.dto.admin.FilterResponseDTO;
import com.digitallending.userservice.model.dto.admin.LenderListDTO;
import com.digitallending.userservice.model.dto.admin.UserFilterTypesDTO;
import com.digitallending.userservice.model.dto.business.BusinessDocumentTypeDTO;
import com.digitallending.userservice.model.dto.business.BusinessTypeDTO;
import com.digitallending.userservice.model.dto.userdetails.RoleTypeStatisticsDTO;
import com.digitallending.userservice.model.dto.userregistration.ChangeUserStatusDTO;
import com.digitallending.userservice.model.dto.userregistration.UserDetailsDTO;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttribute;
import com.digitallending.userservice.model.mapper.UserDetailsMapperImpl;
import com.digitallending.userservice.repository.MsmeUserDetailsAttributeRepository;
import com.digitallending.userservice.repository.UserDetailsRepository;
import com.digitallending.userservice.service.def.BusinessDetailsService;
import com.digitallending.userservice.service.def.BusinessDocumentService;
import com.digitallending.userservice.service.def.BusinessTypeService;
import com.digitallending.userservice.service.impl.UserDetailsServiceImpl;
import com.digitallending.userservice.service.provider.adminVerificationProvider;
import com.digitallending.userservice.service.provider.applyForVerificationProvider;
import com.digitallending.userservice.service.provider.countUserByRoleProvider;
import com.digitallending.userservice.service.provider.getAllVerifiedLendersProvider;
import com.digitallending.userservice.service.provider.getDobByUserIdProvider;
import com.digitallending.userservice.service.provider.getEmailByUserIdProvider;
import com.digitallending.userservice.service.provider.getFilterTypesProvider;
import com.digitallending.userservice.service.provider.getUserDetailsProvider;
import com.digitallending.userservice.service.provider.getUserStatusProvider;
import com.digitallending.userservice.service.provider.updatUserStatusProvider;
import com.digitallending.userservice.service.provider.updateEmailByUserIdProvider;
import com.digitallending.userservice.service.provider.updateUserDetailsProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {
    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private BusinessDetailsService businessDetailsService;
    @Mock
    private BusinessDocumentService businessDocumentService;
    @Mock
    private MsmeUserDetailsAttributeRepository msmeUserDetailsAttributeRepository;
    @Mock
    private BusinessTypeService businessTypeService;




    @InjectMocks
    private UserDetailsMapperImpl userDetailsMapper;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userDetailsService, "userDetailsMapper", userDetailsMapper);
    }

    @Test
    void createUserTest() {
        Date testDate = new Date();
        UserDetails user = UserDetails.builder()
                .userId(UUID.fromString("240ec094-6e5c-4519-85ef-2c7996c2e29e"))
                .email("temp@gmail.com")
                .role(Role.MSME)
                .dob(testDate)
                .build();

        when(userDetailsRepository.save(Mockito.any(UserDetails.class))).thenReturn(user);

        UserDetails createdUser = userDetailsService.createUser(
                "temp@gmail.com",
                testDate,
                Role.MSME,
                UUID.fromString("240ec094-6e5c-4519-85ef-2c7996c2e29e"));
        Assertions.assertEquals(createdUser.getOnBoardingStatus(), UserOnBoardingStatus.SIGN_UP);
        Assertions.assertNotNull(createdUser, "Error creating user");
    }

    @ParameterizedTest
    @ArgumentsSource(getEmailByUserIdProvider.class)
    void getEmailByUserIdTest(Optional<UserDetails> user, boolean shouldThrowError) {
        when(userDetailsRepository.findById(Mockito.any(UUID.class))).thenReturn(user);
        if (shouldThrowError) {
            assertThrows(UserNotFoundException.class, () -> userDetailsService.getEmailByUserId(UUID.randomUUID()));
        } else {
            Assertions.assertEquals(userDetailsService.getEmailByUserId(UUID.randomUUID()), user.get().getEmail());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(updateEmailByUserIdProvider.class)
    void updateEmailByUserIdTest(String email) {
        userDetailsService.updateEmailByUserId(email, UUID.randomUUID());
        verify(userDetailsRepository, times(1))
                .setEmailByUserId(
                        ArgumentMatchers.any(),
                        ArgumentMatchers.eq(email));
    }

    @ParameterizedTest
    @ArgumentsSource(updateUserDetailsProvider.class)
    void updateUserDetailsTest(UserDetails user) {
        when(userDetailsRepository.save(Mockito.any(UserDetails.class))).thenReturn(user);
        UserDetails returnedUser = userDetailsService.updateUserDetails(user);
        assertNotNull(returnedUser);
        assertEquals(returnedUser.getUserId(), user.getUserId());
    }

    @ParameterizedTest
    @ArgumentsSource(getUserDetailsProvider.class)
    void getUserDetailsTest(Optional<UserDetails> user, boolean shouldThrowError) {
        when(userDetailsRepository.findById(Mockito.any(UUID.class))).thenReturn(user);
        if (shouldThrowError) {
            assertThrows(UserNotFoundException.class, () -> userDetailsService.getUserDetails(UUID.randomUUID()));
        } else {
            UserDetails fetchedUser = userDetailsService.getUserDetails(user.get().getUserId());
            assertNotNull(fetchedUser);
            assertEquals(fetchedUser.getUserId(), user.get().getUserId());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(adminVerificationProvider.class)
    void adminVerificationTest(ChangeUserStatusDTO request, Optional<UserDetails> user, boolean shouldThrowError) {
        when(userDetailsRepository.findById(Mockito.any(UUID.class))).thenReturn(user);
        if (shouldThrowError) {
            assertThrows(InvalidUserException.class, () -> userDetailsService.adminVerification(request));
        } else {
            if (request.getOnBoardingStatus().equals(UserOnBoardingStatus.VERIFIED)) {
                assertTrue(userDetailsService.adminVerification(request));
            } else {
                assertFalse(userDetailsService.adminVerification(request));
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(getDobByUserIdProvider.class)
    void getDobByUserIdTest(Optional<UserDetails> user, boolean shouldThrowError) {
        when(userDetailsRepository.findById(Mockito.any(UUID.class))).thenReturn(user);

        if (shouldThrowError) {
            assertThrows(UserNotFoundException.class, () -> userDetailsService.getDobByUserId(UUID.randomUUID()));
        } else {
            Date recievedDate = userDetailsService.getDobByUserId(user.get().getUserId());
            assertNotNull(recievedDate);
            assertEquals(recievedDate, user.get().getDob());
        }
    }

//    @ParameterizedTest
//    @ArgumentsSource(updateDobByUserIdProvider.class)
//    void updateDobByUserIdTest (Optional<UserDetails> user) {
//        when(userDetailsRepository.findById(Mockito.any(UUID.class))).thenReturn(user);
//
//    }

    @ParameterizedTest
    @ArgumentsSource(getUserStatusProvider.class)
    void getUserStatusTest(Optional<UserDetails> user, boolean shouldThrowError) {
        when(userDetailsRepository.findById(Mockito.any(UUID.class))).thenReturn(user);
        if (shouldThrowError) {
            assertThrows(UserNotFoundException.class, () -> userDetailsService.getUserStatus(UUID.randomUUID()));
        } else {
            UserOnBoardingStatus recievedStatus = userDetailsService.getUserStatus(user.get().getUserId());
            assertNotNull(recievedStatus);
            assertEquals(recievedStatus, user.get().getOnBoardingStatus());
        }
    }


    @Test
    void getAllUsersTest() {
        List<UserDetails> userDetailsList = Arrays.asList(new UserDetails(), new UserDetails());
        List<UserDetailsDTO> userDetailsDTOList = Arrays.asList(new UserDetailsDTO(), new UserDetailsDTO());
        Page<UserDetails> pageResponse = new PageImpl<>(userDetailsList, PageRequest.of(0, 2), 4);
        when(userDetailsRepository.findByRoleNot(Mockito.any(Role.class), Mockito.any(Pageable.class))).thenReturn(pageResponse);
        FilterResponseDTO getAll = userDetailsService.getAllUsers(2, 0);

        Assertions.assertEquals(2, getAll.getPageSize());
        Assertions.assertEquals(0, getAll.getPageNo());
        Assertions.assertEquals(4, getAll.getTotalElements());
        Assertions.assertEquals(2, getAll.getTotalPages());
        Assertions.assertNotNull(getAll);
    }


    @ParameterizedTest
    @ArgumentsSource(updatUserStatusProvider.class)
    void updateUserStatusTest(UserOnBoardingStatus status) {
        userDetailsService.updateUserStatus(UUID.randomUUID(), status);

        verify(userDetailsRepository, times(1))
                .setStatusByUserId(
                        ArgumentMatchers.any(),
                        ArgumentMatchers.eq(String.valueOf(status)));
    }

    @ParameterizedTest
    @ArgumentsSource(applyForVerificationProvider.class)
    void applyForVerificationTest(Optional<UserDetails> user, List<BusinessDocumentTypeDTO> listOfDocuments, boolean shouldThrowError) {
        when(userDetailsRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(user);
        if (user.get().getRole().equals(Role.MSME)) {
            when(businessDocumentService.getRemainingDocuments(
                    Mockito.any(UUID.class),
                    Mockito.any(UUID.class)))
                    .thenReturn(listOfDocuments);

            when(businessDetailsService.getBusinessTypeByUserId(Mockito.any(UUID.class))).thenReturn(BusinessType.builder().businessTypeId(UUID.randomUUID()).businessType("temp").build());
        }
        if (shouldThrowError) {
            assertThrows(InvalidUserException.class, () -> userDetailsService.applyForVerification(user.get().getUserId(), user.get().getRole()));
        } else {
            userDetailsService.applyForVerification(user.get().getUserId(), user.get().getRole());
            if (user.get().getOnBoardingStatus().equals(UserOnBoardingStatus.BANK_DETAILS)) {
                verify(userDetailsRepository, times(1)).setStatusByUserId(ArgumentMatchers.any(), ArgumentMatchers.eq("UNVERIFIED"));
            } else if (user.get().getOnBoardingStatus().equals(UserOnBoardingStatus.ON_HOLD)) {
                verify(userDetailsRepository, times(1)).setStatusByUserId(ArgumentMatchers.any(), ArgumentMatchers.eq("REVERIFY"));
            }

        }

    }

    @ParameterizedTest
    @ArgumentsSource(countUserByRoleProvider.class)
    void countUserByRoleTest(List<Object[]> countList) {
        when(userDetailsRepository.findRoleTypes()).thenReturn(countList);

        RoleTypeStatisticsDTO result = userDetailsService.countUserByRole();
        assertNotNull(result);
    }

    @ParameterizedTest
    @ArgumentsSource(getAllVerifiedLendersProvider.class)
    void getALlVerifiedLendersTest(List<UserDetails> listOfLenders){
        when(userDetailsRepository.findAllByRoleAndOnBoardingStatus(
                Mockito.any(Role.class),
                Mockito.any(UserOnBoardingStatus.class))).thenReturn(listOfLenders);

        LenderListDTO result = userDetailsService.getAllVerifiedLenders();
        assertNotNull(result);
        assertEquals(result.getLenderList().size(), listOfLenders.size());
    }

    @ParameterizedTest
    @ArgumentsSource(getFilterTypesProvider.class)
    void getFilterTypesTest(MsmeUserDetailsAttribute attributeReturn, List<BusinessTypeDTO> listOfBusinessTypes) {
        when(msmeUserDetailsAttributeRepository.findByAttributeName(Mockito.anyString()))
                .thenReturn(attributeReturn);
        when(businessTypeService.getAllBusinessTypes()).thenReturn(listOfBusinessTypes);

        UserFilterTypesDTO result = userDetailsService.getFilterTypes();
        assertNotNull(result);
        assertEquals(result.getPropertyTypes().size(),6);
    }


}
