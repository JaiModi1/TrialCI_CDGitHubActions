package com.digitallending.userservice.service;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.BusinessTypeNotExistsException;
import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.exception.PreviousStepsNotDoneException;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.model.dto.business.BusinessDetailsBREDTO;
import com.digitallending.userservice.model.dto.business.BusinessTypeStatisticsDTO;
import com.digitallending.userservice.model.dto.business.SaveBusinessDetailsDTO;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDetails;
import com.digitallending.userservice.repository.BusinessDetailsRepository;
import com.digitallending.userservice.service.def.BusinessDocumentService;
import com.digitallending.userservice.service.def.BusinessTypeService;
import com.digitallending.userservice.service.impl.BusinessDetailsServiceImpl;
import com.digitallending.userservice.service.impl.UserDetailsServiceImpl;
import com.digitallending.userservice.service.provider.countByBusinessTypeProvider;
import com.digitallending.userservice.service.provider.getBREValuesProvider;
import com.digitallending.userservice.service.provider.getBusinessTypeByUserIdProvider;
import com.digitallending.userservice.service.provider.saveBusinessDetailsProvider;
import com.digitallending.userservice.service.provider.updateBusinessDetailsProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BusinessDetailsServiceTest {
    @InjectMocks
    private BusinessDetailsServiceImpl businessDetailsService;

    @Mock
    private BusinessDetailsRepository businessDetailsRepository;
    @Mock
    private BusinessTypeService businessTypeService;
    @Mock
    private BusinessDocumentService businessDocumentService;
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @ParameterizedTest
    @ArgumentsSource(saveBusinessDetailsProvider.class)
    void saveBusinessDetailsTest(BusinessType businessType,
                                 boolean isBusinssPresent,
                                 UserOnBoardingStatus userStatus,
                                 SaveBusinessDetailsDTO businessDetails) {
        when(businessTypeService
                .getBusinessTypeByBusinessTypeId(any(UUID.class)))
                .thenReturn(businessType);
        when(businessTypeService.isBusinessTypePresent(any(BusinessType.class)))
                .thenReturn(isBusinssPresent);

        if (isBusinssPresent) {
            when(userDetailsService
                    .getUserStatus(any(UUID.class))).thenReturn(userStatus);
            if (userStatus.equals(UserOnBoardingStatus.USER_DOC)) {
                ArgumentCaptor<MsmeBusinessDetails> argument = ArgumentCaptor.forClass(MsmeBusinessDetails.class);
                String result = businessDetailsService.saveBusinessDetails(businessDetails, UUID.randomUUID());

                verify(businessDetailsRepository, times(1)).save(argument.capture());

                assertNotNull(result);
                assertNotNull(argument.getValue());
                assertEquals(argument.getValue().getCompanyName(), businessDetails.getCompanyName());
            } else {
                assertThrows(PreviousStepsNotDoneException.class,
                        () -> businessDetailsService
                                .saveBusinessDetails(businessDetails, UUID.randomUUID()));
            }

        } else {
            assertThrows(BusinessTypeNotExistsException.class,
                    () -> businessDetailsService
                            .saveBusinessDetails(businessDetails, UUID.randomUUID()));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(updateBusinessDetailsProvider.class)
    void updateBusinessDetailsTest(UserOnBoardingStatus userStatus,
                                   BusinessType businessType,
                                   boolean isBusinessPresent,
                                   SaveBusinessDetailsDTO businessDetails,
                                   Optional<MsmeBusinessDetails> msmeBizDetails) {
        when(userDetailsService
                .getUserStatus(any(UUID.class)))
                .thenReturn(userStatus);
        when(businessTypeService
                .getBusinessTypeByBusinessTypeId(any(UUID.class)))
                .thenReturn(businessType);

        if (userStatus.compareTo(UserOnBoardingStatus.BUSINESS_DETAILS) < 0 || userStatus.compareTo(UserOnBoardingStatus.ON_HOLD) > 0) {
            assertThrows(UpdateException.class, () -> businessDetailsService
                    .updateBusinessDetails(businessDetails, UUID.randomUUID()));
        } else {
            when(businessTypeService
                    .isBusinessTypePresent(any(BusinessType.class)))
                    .thenReturn(isBusinessPresent);
            if (!isBusinessPresent) {
                assertThrows(BusinessTypeNotExistsException.class,
                        () -> businessDetailsService
                                .updateBusinessDetails(businessDetails, UUID.randomUUID()));
            } else {
                doNothing().when(businessDocumentService).changeBusinessType(
                        any(BusinessType.class),
                        any(BusinessType.class),
                        any(UUID.class)
                );
                when(businessDetailsRepository.findById(any(UUID.class)))
                        .thenReturn(msmeBizDetails);
                ArgumentCaptor<MsmeBusinessDetails> argument = ArgumentCaptor
                        .forClass(MsmeBusinessDetails.class);

                String result = businessDetailsService.updateBusinessDetails(businessDetails, UUID.randomUUID());
                verify(businessDetailsRepository, Mockito.times(1)).save(argument.capture());
                assertNotNull(result);
                assertNotNull(argument.getValue());
                assertEquals(argument.getValue().getCompanyName(), businessDetails.getCompanyName());
            }
        }
    }


    @ParameterizedTest
    @ArgumentsSource(getBREValuesProvider.class)
    void getBREValuesTest(Optional<MsmeBusinessDetails> businessDetails, boolean shoudlThrowError) {
        when(businessDetailsRepository
                .findById(any(UUID.class))).thenReturn(businessDetails);
        if (shoudlThrowError) {
            assertThrows(DetailsNotFoundException.class,
                    () -> businessDetailsService.getBREValues(UUID.randomUUID()));
        } else {
            BusinessDetailsBREDTO result = businessDetailsService.getBREValues(UUID.randomUUID());
            assertNotNull(result);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(getBusinessTypeByUserIdProvider.class)
    void getBusinssTypeByUserIdTest(Optional<MsmeBusinessDetails> businessDetails, boolean shoudlThrowError) {
        when(businessDetailsRepository
                .findById(any(UUID.class))).thenReturn(businessDetails);
        if (shoudlThrowError) {
            assertThrows(DetailsNotFoundException.class,
                    () -> businessDetailsService.getBusinessTypeByUserId(UUID.randomUUID()));
        } else {
            BusinessType result = businessDetailsService.getBusinessTypeByUserId(UUID.randomUUID());
            assertNotNull(result);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(countByBusinessTypeProvider.class)
    void countByBusinessTypeTest(List<Object[]> businessTypes) {
        when(businessDetailsRepository.findBusinessTypes())
                .thenReturn(businessTypes);

        when(businessTypeService
                .getBusinessTypeByBusinessTypeId(
                        UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .thenReturn(BusinessType
                        .builder()
                        .businessType("Partnership")
                        .build());
        when(businessTypeService
                .getBusinessTypeByBusinessTypeId(
                        UUID.fromString("00000000-0000-0000-0000-000000000002")))
                .thenReturn(BusinessType
                        .builder()
                        .businessType("Private Limited Company")
                        .build());
        when(businessTypeService
                .getBusinessTypeByBusinessTypeId(
                        UUID.fromString("00000000-0000-0000-0000-000000000003")))
                .thenReturn(BusinessType
                        .builder()
                        .businessType("Public Limited Company")
                        .build());
        when(businessTypeService
                .getBusinessTypeByBusinessTypeId(
                        UUID.fromString("00000000-0000-0000-0000-000000000004")))
                .thenReturn(BusinessType
                        .builder()
                        .businessType("Sole Proprietorship")
                        .build());

        BusinessTypeStatisticsDTO businessTypeStats = businessDetailsService.countByBusinessType();

        assertNotNull(businessTypeStats);
        assertTrue(businessTypeStats.getPrivatLC() > 0L);
        assertTrue(businessTypeStats.getPartnership() > 0L);
        assertTrue(businessTypeStats.getPublicLC() > 0L);
        assertTrue(businessTypeStats.getSolePropritership() > 0L);

    }


}
