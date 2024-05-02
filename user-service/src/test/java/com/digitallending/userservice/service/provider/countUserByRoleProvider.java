package com.digitallending.userservice.service.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class countUserByRoleProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        List<Object[]> list = new ArrayList<>();

        List<Object> data1 = Arrays.asList("MSME",10L);
        List<Object> data2 = Arrays.asList("LENDER",10L);

        list.add(data1.toArray());
        list.add(data2.toArray());


        return Stream.of(
                Arguments.of(list)
        );
    }
}
