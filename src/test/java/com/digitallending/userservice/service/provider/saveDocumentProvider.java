package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.model.dto.business.BusinessDocumentTypeDTO;
import com.digitallending.userservice.model.entity.BusinessDocumentType;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDocument;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class saveDocumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        BusinessDocumentType partnerId =  BusinessDocumentType
                .builder()
                .businessDocumentType("Partner ID Proof")
                .businessDocumentTypeId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .businessTypeList(new ArrayList<>())
                .build();

        BusinessDocumentType otherType = BusinessDocumentType
                .builder()
                .businessDocumentType("other")
                .businessDocumentTypeId(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .businessTypeList(new ArrayList<>())
                .build();

        List<BusinessDocumentType> list = Arrays.asList(
                partnerId,
                otherType
        );
        BusinessType inList = BusinessType
                .builder()
                .documentTypeList(list)
                .businessTypeId(UUID.randomUUID())
                .build();
        BusinessType notInList = BusinessType
                .builder()
                .documentTypeList(new ArrayList<>())
                .businessTypeId(UUID.randomUUID())
                .build();

        UserOnBoardingStatus businessDetails = UserOnBoardingStatus.BUSINESS_DETAILS;
        UserOnBoardingStatus less = UserOnBoardingStatus.USER_DETAILS;
        UserOnBoardingStatus more = UserOnBoardingStatus.REVERIFY;
        UserOnBoardingStatus update = UserOnBoardingStatus.BANK_DETAILS;


        MockMultipartFile nullContent = new MockMultipartFile(
                "Name", new byte[]{});
        MockMultipartFile documentContent = new MockMultipartFile(
                "Name", new byte[]{1});




        Optional<BusinessDocumentType> optionalPartner = Optional.of(partnerId);
        Optional<BusinessDocumentType> optionalOther = Optional.of(otherType);
        Optional<BusinessDocumentType> nullDocType = Optional.ofNullable(null);


        MsmeBusinessDocument nullDocument = null;
        MsmeBusinessDocument validDocument = MsmeBusinessDocument.builder().build();


        MsmeBusinessDocument savedDoc = MsmeBusinessDocument.builder().build();


        UserDetails user = UserDetails.builder().build();

        List<BusinessDocumentTypeDTO> emptyList = new ArrayList<>();
        List<BusinessDocumentTypeDTO> partnerList = Arrays.asList(
                BusinessDocumentTypeDTO
                        .builder()
                        .businessDocumentTypeId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                        .businessDocumentType("Partner ID Proof")
                        .build()
        );
        List<BusinessDocumentTypeDTO> fullList = Arrays.asList(
                BusinessDocumentTypeDTO
                        .builder()
                        .businessDocumentTypeId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                        .businessDocumentType("Partner ID Proof")
                        .build(),
                BusinessDocumentTypeDTO
                        .builder()
                        .businessDocumentTypeId(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                        .businessDocumentType("else")
                        .build()
        );



        return Stream.of(
                // correct flow
                Arguments.of(
                        inList,
                        businessDetails,
                        documentContent,
                        optionalPartner,
                        nullDocument,
                        false,
                        false,
                        savedDoc,
                        user,
                        fullList,
                        false,
                        5),
                // document invalid
                Arguments.of(
                        inList,
                        businessDetails,
                        nullContent,
                        optionalPartner,
                        nullDocument,
                        false,
                        false,
                        savedDoc,
                        user,
                        fullList,
                        false,
                        5),
                // less than BUSINESS_DETAILS
                Arguments.of(
                        inList,
                        less,
                        documentContent,
                        optionalPartner,
                        nullDocument,
                        false,
                        false,
                        savedDoc,
                        user,
                        fullList,
                        false,
                        5),
                // more than REVERIFY
                Arguments.of(
                        inList,
                        more,
                        documentContent,
                        optionalPartner,
                        nullDocument,
                        false,
                        false,
                        savedDoc,
                        user,
                        fullList,
                        false,
                        5),
                // documentType not present
                Arguments.of(
                        inList,
                        businessDetails,
                        documentContent,
                        nullDocType,
                        nullDocument,
                        false,
                        false,
                        savedDoc,
                        user,
                        fullList,
                        false,
                        5),
                // re entry attempt
                Arguments.of(
                        inList,
                        businessDetails,
                        documentContent,
                        optionalPartner,
                        validDocument,
                        false,
                        false,
                        savedDoc,
                        user,
                        fullList,
                        false,
                        5),
                // document not required
                Arguments.of(
                        notInList,
                        businessDetails,
                        documentContent,
                        optionalPartner,
                        nullDocument,
                        false,
                        true,
                        savedDoc,
                        user,
                        fullList,
                        false,
                        5),
                Arguments.of(
                        inList,
                        businessDetails,
                        documentContent,
                        optionalPartner,
                        nullDocument,
                        false,
                        false,
                        savedDoc,
                        user,
                        emptyList,
                        true,
                        5)
        );
    }
}