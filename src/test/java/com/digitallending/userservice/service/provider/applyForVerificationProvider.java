package com.digitallending.userservice.service.provider;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.model.dto.business.BusinessDocumentTypeDTO;
import com.digitallending.userservice.model.entity.UserDetails;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class applyForVerificationProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        List<BusinessDocumentTypeDTO> correctlistOfDoc = new ArrayList<>();
        List<BusinessDocumentTypeDTO> justRightListOfDoc = Arrays.asList(
                BusinessDocumentTypeDTO
                        .builder()
                        .businessDocumentType("Partner ID Proof")
                        .businessDocumentTypeId(UUID.randomUUID())
                        .build());
        List<BusinessDocumentTypeDTO> incorrectListOfDoc = Arrays.asList(new BusinessDocumentTypeDTO(),new BusinessDocumentTypeDTO());

        UserDetails user = UserDetails.builder().role(Role.MSME).userId(UUID.randomUUID()).onBoardingStatus(UserOnBoardingStatus.ON_HOLD).build();
        Optional<UserDetails> optionalUser = Optional.of(user);

        UserDetails user2 = UserDetails.builder().role(Role.MSME).userId(UUID.randomUUID()).onBoardingStatus(UserOnBoardingStatus.BANK_DETAILS).build();
        Optional<UserDetails> optionalUser2 = Optional.of(user2);

        UserDetails user3 = UserDetails.builder().role(Role.LENDER).userId(UUID.randomUUID()).onBoardingStatus(UserOnBoardingStatus.BANK_DETAILS).build();
        Optional<UserDetails> optionalUser3 = Optional.of(user3);

        UserDetails user4 = UserDetails.builder().role(Role.LENDER).userId(UUID.randomUUID()).onBoardingStatus(UserOnBoardingStatus.ON_HOLD).build();
        Optional<UserDetails> optionalUser4 = Optional.of(user4);

        UserDetails user5 = UserDetails.builder().role(Role.MSME).userId(UUID.randomUUID()).onBoardingStatus(UserOnBoardingStatus.SIGN_UP).build();
        Optional<UserDetails> optionalUser5 = Optional.of(user5);


        return Stream.of(
                Arguments.of(optionalUser, correctlistOfDoc,false),
                Arguments.of(optionalUser2, justRightListOfDoc,false),
                Arguments.of(optionalUser3, correctlistOfDoc,false),
                Arguments.of(optionalUser4, justRightListOfDoc,false),
                Arguments.of(optionalUser5, justRightListOfDoc,true),
                Arguments.of(optionalUser, incorrectListOfDoc,true)
        );
    }
}
