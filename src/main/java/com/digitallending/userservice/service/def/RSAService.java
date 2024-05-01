package com.digitallending.userservice.service.def;

import org.springframework.stereotype.Service;

@Service
public interface RSAService {


    String getPublicKey();

    String decodeMessage(String encodedString);

    String encodeMessage(String string);


}
