package com.digitallending.userservice.service.def;

import com.digitallending.userservice.exception.DetailsNotFoundException;

import java.util.UUID;

public interface ChangePasswordTokenService {
    UUID saveEntry(String email);

    String getEmail(UUID token) throws DetailsNotFoundException;
}
