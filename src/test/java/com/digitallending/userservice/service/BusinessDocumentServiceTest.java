package com.digitallending.userservice.service;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.exception.DocumentTypeNotFoundException;
import com.digitallending.userservice.exception.EmptyDocumentException;
import com.digitallending.userservice.exception.InvalidUserException;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.model.dto.business.BusinessDocumentTypeDTO;
import com.digitallending.userservice.model.entity.BusinessDocumentType;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDocument;
import com.digitallending.userservice.repository.BusinessDocumentRepository;
import com.digitallending.userservice.repository.BusinessDocumentTypeRepository;
import com.digitallending.userservice.repository.BusinessTypeRepository;
import com.digitallending.userservice.service.def.UserDetailsService;
import com.digitallending.userservice.service.impl.BusinessDetailsServiceImpl;
import com.digitallending.userservice.service.impl.BusinessDocumentServiceImpl;
import com.digitallending.userservice.service.provider.changeBusinessTypeProvider;
import com.digitallending.userservice.service.provider.saveDocumentProvider;
import com.digitallending.userservice.service.provider.updateDocumentProvider;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BusinessDocumentServiceTest {

    @InjectMocks
    private BusinessDocumentServiceImpl businessDocumentService;

    @Mock
    private BusinessDocumentRepository businessDocumentRepository;
    @Mock
    private BusinessDocumentTypeRepository businessDocumentTypeRepository;
    @Mock
    private BusinessDetailsServiceImpl businessDetailsService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private BusinessTypeRepository businessTypeRepository;

    @ParameterizedTest
    @ArgumentsSource(saveDocumentProvider.class)
    void saveDocumentTest(BusinessType businessType,
                          UserOnBoardingStatus userStatus,
                          MockMultipartFile documentContent,
                          Optional<BusinessDocumentType> optionalDocType,
                          MsmeBusinessDocument businessDocument,
                          boolean twoEntryError,
                          boolean docNotRequired,
                          MsmeBusinessDocument savedDoc,
                          UserDetails user,
                          List<BusinessDocumentTypeDTO> listOfRemainingDocs,
                          boolean updateUserStatus, int count) throws IOException {

        BusinessDocumentServiceImpl spy = Mockito.spy(businessDocumentService);
        if (documentContent.isEmpty()) {
            assertThrows(EmptyDocumentException.class,
                    () -> businessDocumentService
                            .saveDocument(
                                    documentContent,
                                    UUID.randomUUID(),
                                    UUID.randomUUID()));
        } else {
            when(businessDetailsService
                    .getBusinessTypeByUserId(any(UUID.class)))
                    .thenReturn(businessType);

            when(userDetailsService
                    .getUserStatus(any(UUID.class)))
                    .thenReturn(userStatus);
            if (userStatus.compareTo(UserOnBoardingStatus.BUSINESS_DETAILS) < 0 ||
                    userStatus.compareTo(UserOnBoardingStatus.REVERIFY) >= 0) {
                assertThrows(InvalidUserException.class,
                        () -> businessDocumentService
                                .saveDocument(
                                        documentContent,
                                        UUID.randomUUID(),
                                        UUID.randomUUID()));
            } else {
                when(businessDocumentTypeRepository
                        .findById(any(UUID.class)))
                        .thenReturn(optionalDocType);
                if (optionalDocType.isEmpty()) {
                    assertThrows(DetailsNotFoundException.class,
                            () -> businessDocumentService.saveDocument(
                                    documentContent,
                                    UUID.randomUUID(),
                                    UUID.randomUUID()));
                } else {
                    if (!optionalDocType.isEmpty() &&
                            !optionalDocType.get()
                                    .getBusinessDocumentType().equals("Partner ID Proof")) {
                        when(businessDocumentRepository
                                .findByUserUserIdAndBusinessDocumentTypeBusinessDocumentTypeId(
                                        any(UUID.class),
                                        any(UUID.class)
                                )).thenReturn(businessDocument);
                    }
                    if (twoEntryError) {
                        assertThrows(InvalidUserException.class,
                                () -> businessDocumentService.saveDocument(
                                        documentContent,
                                        UUID.randomUUID(),
                                        UUID.randomUUID()));
                    } else if (docNotRequired) {
                        assertThrows(DocumentTypeNotFoundException.class,
                                () -> businessDocumentService.saveDocument(
                                        documentContent,
                                        UUID.randomUUID(),
                                        UUID.randomUUID()));
                    } else {
                        when(userDetailsService
                                .getUserDetails(any(UUID.class)))
                                .thenReturn(user);
                        when(businessDocumentRepository
                                .save(any(MsmeBusinessDocument.class)))
                                .thenReturn(savedDoc);
                        Mockito.doReturn(listOfRemainingDocs).when(spy)
                                .getRemainingDocuments(
                                        any(UUID.class),
                                        any(UUID.class));
                        if (listOfRemainingDocs.size() == 1) {
                            when(businessDocumentRepository
                                    .countByUserUserId(any(UUID.class)))
                                    .thenReturn(count);
                        }
                        if (updateUserStatus) {
                            Assert.notNull(spy.saveDocument(
                                    documentContent,
                                    UUID.randomUUID(),
                                    UUID.randomUUID()
                            ));
                            verify(userDetailsService, times(1))
                                    .updateUserStatus(
                                            ArgumentMatchers.any(),
                                            ArgumentMatchers.eq(UserOnBoardingStatus.BUSINESS_DOC));
                        } else {
                            Assert.notNull(spy.saveDocument(
                                    documentContent,
                                    UUID.randomUUID(),
                                    UUID.randomUUID()
                            ));
                        }

                    }

                }
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(updateDocumentProvider.class)
    void updateDocumentTest(MockMultipartFile documentContent,
                            UserOnBoardingStatus userStatus,
                            Optional<MsmeBusinessDocument> documentRow,
                            boolean incorrectUser) throws IOException {
        if (documentContent.isEmpty()) {
            assertThrows(EmptyDocumentException.class,
                    () -> businessDocumentService
                            .updateDocument(
                                    documentContent,
                                    UUID.randomUUID(),
                                    UUID.randomUUID()));
        } else {
            when(userDetailsService
                    .getUserStatus(any(UUID.class)))
                    .thenReturn(userStatus);
            if (userStatus.compareTo(UserOnBoardingStatus.BUSINESS_DETAILS) < 0
                    || userStatus.compareTo(UserOnBoardingStatus.ON_HOLD) > 0) {
                assertThrows(UpdateException.class,
                        () -> businessDocumentService.updateDocument(
                                documentContent,
                                UUID.randomUUID(),
                                UUID.randomUUID()));
            } else {
                when(businessDocumentRepository.findById(any(UUID.class))).thenReturn(documentRow);
                if (documentRow.isEmpty()) {
                    assertThrows(DetailsNotFoundException.class, () -> businessDocumentService
                            .updateDocument(
                                    documentContent,
                                    UUID.randomUUID(),
                                    UUID.randomUUID()));
                }
                if (incorrectUser) {
                    assertThrows(InvalidUserException.class,
                            () -> businessDocumentService
                                    .updateDocument(
                                            documentContent,
                                            UUID.randomUUID(),
                                            UUID.randomUUID()));
                } else {
                    MsmeBusinessDocument result = businessDocumentService.updateDocument(
                            documentContent,
                            UUID.randomUUID(),
                            documentRow.get().getUser().getUserId());
                    ArgumentCaptor<MsmeBusinessDocument> argument = ArgumentCaptor.forClass(MsmeBusinessDocument.class);
                    verify(businessDocumentRepository).save(argument.capture());
                    assertNotNull(argument.getValue());
                    assertEquals(argument.getValue().getUser().getUserId(), documentRow.get().getUser().getUserId());
                }
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(changeBusinessTypeProvider.class)
    void changeBusinessTypeTest(BusinessType one,
                                BusinessType two,
                                BusinessType old,
                                BusinessType next,
                                int check,
                                List<MsmeBusinessDocument> submittedList,
                                int removal,
                                UserOnBoardingStatus userStatus) {
        System.out.println(old.getBusinessType());
        System.out.println(next.getBusinessType());
        if (old.getBusinessType().equals("one") || next.getBusinessType().equals("one")) {
            when(businessTypeRepository.findById(one.getBusinessTypeId())).thenReturn(Optional.of(one));
        }
        if (old.getBusinessType().equals("two") || next.getBusinessType().equals("two")) {
            when(businessTypeRepository.findById(two.getBusinessTypeId())).thenReturn(Optional.of(two));
        }


        if (check == 0) {
            businessDocumentService.changeBusinessType(old, next, UUID.randomUUID());
            verify(businessDocumentRepository, never())
                    .deleteByUserUserIdAndBusinessDocumentTypeBusinessDocumentTypeId(any(UUID.class), any(UUID.class));
            verify(userDetailsService, never()).updateUserStatus(ArgumentMatchers.any(), ArgumentMatchers.any());
        } else {
            when(businessDocumentRepository
                    .findByUserUserId(any(UUID.class)))
                    .thenReturn(submittedList);
            if (removal != 0) {
                verify(businessDocumentRepository, times(removal))
                        .deleteByUserUserIdAndBusinessDocumentTypeBusinessDocumentTypeId(any(UUID.class), any(UUID.class));
//                doNothing().when(businessDocumentRepository)
//                        .deleteByUserUserIdAndBusinessDocumentTypeBusinessDocumentTypeId(
//                                any(UUID.class),any(UUID.class));
            }
            if (userStatus.equals(UserOnBoardingStatus.BUSINESS_DETAILS)) {
                when(userDetailsService.getUserStatus(any(UUID.class)))
                        .thenReturn(userStatus);
                ArgumentCaptor<UserOnBoardingStatus> argument = ArgumentCaptor
                        .forClass(UserOnBoardingStatus.class);
                businessDocumentService.changeBusinessType(old, next, UUID.randomUUID());
                verify(userDetailsService, times(1)).updateUserStatus(ArgumentMatchers.any(), argument.capture());
                assertNotNull(argument.getValue());
                System.out.println(argument.getValue());
                assertEquals(argument.getValue(), UserOnBoardingStatus.BUSINESS_DOC);

            }

        }
    }


}
