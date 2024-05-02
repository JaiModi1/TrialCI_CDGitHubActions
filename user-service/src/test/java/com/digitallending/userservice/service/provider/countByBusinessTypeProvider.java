package com.digitallending.userservice.service.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class countByBusinessTypeProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        List<Object[]> data = new ArrayList<>();
        List<Object> entry1 = Arrays.asList(UUID.fromString("00000000-0000-0000-0000-000000000001"),10L);
        List<Object> entry2 = Arrays.asList(UUID.fromString("00000000-0000-0000-0000-000000000002"),11L);
        List<Object> entry3 = Arrays.asList(UUID.fromString("00000000-0000-0000-0000-000000000003"),12L);
        List<Object> entry4 = Arrays.asList(UUID.fromString("00000000-0000-0000-0000-000000000004"),13L);

        data.add(entry1.toArray());
        data.add(entry2.toArray());
        data.add(entry3.toArray());
        data.add(entry4.toArray());



        return Stream.of(
                Arguments.of(data)
        );
    }
}
