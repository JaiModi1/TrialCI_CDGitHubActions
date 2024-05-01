package com.digitallending.userservice.controller;


import com.digitallending.userservice.model.dto.apiresponse.APIResponseDTO;
import com.digitallending.userservice.service.def.RSAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/security")
public class RSAController {

    @Autowired
    RSAService rsaService;


    @GetMapping
    public ResponseEntity<APIResponseDTO> getPublicKey() {
        return new ResponseEntity<>(APIResponseDTO
                .builder()
                .payload(rsaService.getPublicKey())
                .build(), HttpStatus.OK);
    }
}
