package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.model.entity.ChangePasswordToken;
import com.digitallending.userservice.repository.ChangePasswordTokenRepository;
import com.digitallending.userservice.service.def.ChangePasswordTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ChangePasswordTokenServiceImpl implements ChangePasswordTokenService {

    @Autowired
    private ChangePasswordTokenRepository changePasswordTokenRepository;

    public UUID saveEntry(String email) {
        UUID token = UUID.randomUUID();
        ChangePasswordToken entry = new ChangePasswordToken();
        entry.setEmail(email);
        entry.setToken(token);

        changePasswordTokenRepository.save(entry);
        return token;
    }

    public String getEmail(UUID token) throws DetailsNotFoundException {
        Optional<ChangePasswordToken> entry = changePasswordTokenRepository.findById(token);
        return entry.map(ChangePasswordToken::getEmail).orElseThrow(() -> new DetailsNotFoundException("Can Not match the token, Can not update password"));
    }
}
