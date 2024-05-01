package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.exception.UserNotFoundException;
import com.digitallending.userservice.model.entity.UserKYCDetails;
import com.digitallending.userservice.repository.UserKYCDetailsRepository;
import com.digitallending.userservice.service.def.UserKYCDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserKYCDetailsServiceImpl implements UserKYCDetailsService {

    @Autowired
    private UserKYCDetailsRepository userKYCDetailsRepository;

    @Override
    public UserKYCDetails getUserKYCDetailsByUserId(UUID userId) {
        return userKYCDetailsRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Exist"));
    }

    @Override
    public UserKYCDetails saveUserKYCDetailsByUserId(UserKYCDetails userKYCDetails) {
        return userKYCDetailsRepository.save(userKYCDetails);
    }
}
