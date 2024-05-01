package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.model.dto.business.BusinessTypeDTO;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttribute;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttributeValue;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class getFilterTypesProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        List<MsmeUserDetailsAttributeValue> listOfValues = new ArrayList<>();
        listOfValues.add(MsmeUserDetailsAttributeValue
                .builder()
                .attributeValueId(UUID.randomUUID())
                .value("Val 1")
                .build());
        listOfValues.add(MsmeUserDetailsAttributeValue
                .builder()
                .attributeValueId(UUID.randomUUID())
                .value("Val 2")
                .build());

        MsmeUserDetailsAttribute attributeReturn = MsmeUserDetailsAttribute
                .builder()
                .attributeId(UUID.randomUUID())
                .attributeName("Temp Attribute Name")
                .msmeUserDetailsAttributeValues(listOfValues)
                .build();

        List<BusinessTypeDTO> listOfBusinesses = new ArrayList<>();
        listOfBusinesses.add(BusinessTypeDTO
                .builder()
                .businessType("Type 1")
                .businessTypeId(UUID.randomUUID())
                .build());
        listOfBusinesses.add(BusinessTypeDTO
                .builder()
                .businessType("Type 2")
                .businessTypeId(UUID.randomUUID())
                .build());

        return Stream.of(
                Arguments.of(attributeReturn, listOfBusinesses)
        );
    }
}
