package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.model.dto.business.SaveBusinessDetailsDTO;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDetails;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class updateBusinessDetailsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        BusinessType businessType = BusinessType
                .builder()
                .businessTypeId(UUID.randomUUID())
                .businessType("Temp")
                .build();
        MsmeBusinessDetails buizDetails = MsmeBusinessDetails.builder().businessType(businessType).userId(UUID.randomUUID()).build();
        Optional<MsmeBusinessDetails> optionalDetail = Optional.of(buizDetails);
        SaveBusinessDetailsDTO businessDetails = SaveBusinessDetailsDTO
                .builder()
                .companyName("temp")
                .businessTypeId(UUID.randomUUID())
                .build();
        return Stream.of(
                // full correct
                Arguments.of(UserOnBoardingStatus.BANK_DETAILS, businessType, true,businessDetails,optionalDetail),
                // business type not present
                Arguments.of(UserOnBoardingStatus.BANK_DETAILS, businessType, false,businessDetails,optionalDetail),
                // invalid status
                Arguments.of(UserOnBoardingStatus.USER_DOC, businessType, true,businessDetails,optionalDetail)
        );
    }
}
