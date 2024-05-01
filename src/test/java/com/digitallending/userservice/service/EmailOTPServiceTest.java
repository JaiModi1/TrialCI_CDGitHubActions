package com.digitallending.userservice.service;

import com.digitallending.userservice.exception.WrongOTPException;
import com.digitallending.userservice.model.entity.EmailOTP;
import com.digitallending.userservice.repository.EmailOTPRepository;
import com.digitallending.userservice.service.impl.EmailOTPServiceImpl;
import com.digitallending.userservice.service.provider.saveOTPProvider;
import com.digitallending.userservice.service.provider.verifyOTPProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmailOTPServiceTest {

    @InjectMocks
    private EmailOTPServiceImpl emailOTPService;

    @Mock
    private EmailOTPRepository emailOTPRepository;

    @ParameterizedTest
    @ArgumentsSource(saveOTPProvider.class)
    void saveOTPTest(String email, String otp) {

        ArgumentCaptor<EmailOTP> argument = ArgumentCaptor.forClass(EmailOTP.class);

        emailOTPService.saveOTP(email,otp);
        verify(emailOTPRepository, times(1)).save(argument.capture());

        Assertions.assertEquals(argument.getValue().getOtp(), otp);
        Assertions.assertEquals(argument.getValue().getEmailId(), email);
    }

    @ParameterizedTest
    @ArgumentsSource(verifyOTPProvider.class)
    void verifyOTPTest(EmailOTP reference, boolean shouldThrowError){
        when(emailOTPRepository.getReferenceById(Mockito.anyString())).thenReturn(reference);


        if(shouldThrowError){
            Assertions.assertThrows(WrongOTPException.class, () -> emailOTPService.verifyOTP("temp","1234"));
        }
        else{
            doNothing().when(emailOTPRepository).delete(Mockito.any(EmailOTP.class));
            boolean result = emailOTPService.verifyOTP(reference.getEmailId(), reference.getOtp());
            Assertions.assertTrue(result);
        }

    }
}
