package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.model.entity.BusinessDocumentType;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDocument;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class changeBusinessTypeProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
//
        BusinessDocumentType alpha = BusinessDocumentType
                .builder()
                .businessDocumentType("alpha")
                .businessDocumentTypeId(UUID.fromString("00000000-0000-0000-0000-000000000010"))
                .build();
        BusinessDocumentType beta = BusinessDocumentType
                .builder()
                .businessDocumentType("beta")
                .businessDocumentTypeId(UUID.fromString("00000000-0000-0000-0000-000000000020"))
                .build();
        BusinessType one = BusinessType
                .builder()
                .businessType("one")
                .businessTypeId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .documentTypeList(Arrays.asList(alpha))
                .build();
        BusinessType two = BusinessType
                .builder()
                .businessType("two")
                .businessTypeId(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .documentTypeList(Arrays.asList(alpha,beta))
                .build();

        MsmeBusinessDocument doc1 = MsmeBusinessDocument.builder().build();
        List<MsmeBusinessDocument> emptySubmittedDoc = new ArrayList<>();
        List<MsmeBusinessDocument> fullySubmittedTwo = new ArrayList<>();
        fullySubmittedTwo.add(MsmeBusinessDocument.builder().businessDocumentType(alpha).build());
        fullySubmittedTwo.add(MsmeBusinessDocument.builder().businessDocumentType(beta).build());

        List<MsmeBusinessDocument> fullySubmittedOne = new ArrayList<>();
        fullySubmittedOne.add(MsmeBusinessDocument.builder().businessDocumentType(alpha).build());


        return Stream.of(
                Arguments.of(
                        one,two,one,one,0,emptySubmittedDoc,0,UserOnBoardingStatus.BUSINESS_DOC
                ),
                Arguments.of(
                        one,two,two,one,0,emptySubmittedDoc,0,UserOnBoardingStatus.BUSINESS_DOC
                ),
                Arguments.of(
                        one,two,two,one,1,fullySubmittedOne,0,UserOnBoardingStatus.BUSINESS_DOC
                )

        );
    }
//    BusinessType one,
//    BusinessType two,
//    BusinessType old,
//    BusinessType next,
//    int check,
//    List<MsmeBusinessDocument> submittedList,
//    int removal,
//    UserOnBoardingStatus userStatus
}
