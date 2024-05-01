package com.digitallending.userservice.service;

import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.model.entity.ChangePasswordToken;
import com.digitallending.userservice.repository.ChangePasswordTokenRepository;
import com.digitallending.userservice.service.impl.ChangePasswordTokenServiceImpl;
import com.digitallending.userservice.service.provider.getEmailProvider;
import com.digitallending.userservice.service.provider.saveEntryProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChangePasswordTokenServiceTest {

    @InjectMocks
    private ChangePasswordTokenServiceImpl changePasswordTokenService;

    @Mock
    private ChangePasswordTokenRepository changePasswordTokenRepository;

    @ParameterizedTest
    @ArgumentsSource(saveEntryProvider.class)
    void saveEntryTest(String email){
        ArgumentCaptor<ChangePasswordToken> argument = ArgumentCaptor
                .forClass(ChangePasswordToken.class);

        UUID result = changePasswordTokenService.saveEntry(email);

        Mockito.verify(changePasswordTokenRepository, Mockito.times(1)).save(argument.capture());
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(argument.getValue());
        Assertions.assertEquals(argument.getValue().getEmail(), email);
    }

    @ParameterizedTest
    @ArgumentsSource(getEmailProvider.class)
    void getEmailTest(Optional<ChangePasswordToken> entry, boolean shouldThrowError) {
        when(changePasswordTokenRepository
                .findById(Mockito.any(UUID.class)))
                .thenReturn(entry);

        if(shouldThrowError){
            Assertions.assertThrows(DetailsNotFoundException.class, () -> changePasswordTokenService.getEmail(UUID.randomUUID()));
        }
        else{
            String result = changePasswordTokenService.getEmail(entry.get().getToken());
            Assertions.assertEquals(result, entry.get().getEmail());
        }

    }
}
