package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDetails;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class getBusinessTypeByUserIdProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        MsmeBusinessDetails businessDetails = MsmeBusinessDetails
                .builder()
                .businessExperience(new Date())
                .registrationDate(new Date())
                .businessType(BusinessType
                        .builder()
                        .businessTypeId(UUID.randomUUID())
                        .businessType("String")
                        .build())
                .build();

        Optional<MsmeBusinessDetails> correctDetails = Optional.of(businessDetails);
        Optional<MsmeBusinessDetails> nullDetails = Optional.ofNullable(null);
        return Stream.of(
                Arguments.of(correctDetails, false),
                Arguments.of(nullDetails, true)
        );
    }
}
