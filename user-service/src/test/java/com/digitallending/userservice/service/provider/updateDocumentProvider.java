package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDocument;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class updateDocumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        MockMultipartFile nullContent = new MockMultipartFile(
                "Name", new byte[]{});
        MockMultipartFile documentContent = new MockMultipartFile(
                "Name", new byte[]{1});

        MsmeBusinessDocument documentData = MsmeBusinessDocument.builder().user(UserDetails.builder().userId(UUID.randomUUID()).build()).build();
        return Stream.of(
                Arguments.of(documentContent, UserOnBoardingStatus.BANK_DETAILS, Optional.of(documentData), false),
                Arguments.of(nullContent, UserOnBoardingStatus.BANK_DETAILS, Optional.of(documentData), false),
                Arguments.of(documentContent, UserOnBoardingStatus.USER_DETAILS, Optional.of(documentData), false),
                Arguments.of(documentContent, UserOnBoardingStatus.VERIFIED, Optional.of(documentData), false),
                Arguments.of(documentContent, UserOnBoardingStatus.BANK_DETAILS, Optional.of(documentData), true)
        );
    }

}
