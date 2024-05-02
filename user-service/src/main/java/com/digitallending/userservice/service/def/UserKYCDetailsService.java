package com.digitallending.userservice.service.def;

import com.digitallending.userservice.model.entity.UserKYCDetails;

import java.util.UUID;

public interface UserKYCDetailsService {

    UserKYCDetails getUserKYCDetailsByUserId(UUID userId);

    UserKYCDetails saveUserKYCDetailsByUserId(UserKYCDetails userKYCDetails);

}
