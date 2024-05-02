package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.model.dto.business.SaveBusinessDetailsDTO;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.UUID;
import java.util.stream.Stream;

public class saveBusinessDetailsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        BusinessType businessType = BusinessType
                .builder()
                .businessTypeId(UUID.randomUUID())
                .businessType("Temp")
                .build();

        SaveBusinessDetailsDTO businessDetails = SaveBusinessDetailsDTO
                .builder()
                .businessTypeId(UUID.randomUUID())
                .companyName("Attempt")
                .build();

        return Stream.of(
                Arguments.of(businessType,true, UserOnBoardingStatus.USER_DOC,businessDetails),
                Arguments.of(businessType,false, UserOnBoardingStatus.USER_DOC,businessDetails),
                Arguments.of(businessType,true, UserOnBoardingStatus.USER_DETAILS,businessDetails)
        );
    }
}
