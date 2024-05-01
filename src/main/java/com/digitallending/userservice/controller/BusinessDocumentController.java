package com.digitallending.userservice.controller;

import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.model.dto.apiresponse.APIResponseDTO;
import com.digitallending.userservice.model.dto.business.BusinessDocumentDetailsDTO;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDocument;
import com.digitallending.userservice.repository.BusinessDocumentTypeRepository;
import com.digitallending.userservice.service.def.BusinessDetailsService;
import com.digitallending.userservice.service.def.BusinessDocumentService;
import com.digitallending.userservice.service.def.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/business-document")
public class BusinessDocumentController {

    @Autowired
    private BusinessDocumentService businessDocumentService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BusinessDetailsService businessDetailsService;

    @Autowired
    private BusinessDocumentTypeRepository businessDocumentTypeRepository;


    @GetMapping
    public ResponseEntity<APIResponseDTO<BusinessDocumentDetailsDTO>> getAllBusinessDocumentDetails(@RequestHeader("UserId") UUID userId) throws DetailsNotFoundException {

        BusinessDocumentDetailsDTO response = businessDocumentService.getBusinessDocumentDetailsByUserId(userId);

        return new ResponseEntity<>(APIResponseDTO.<BusinessDocumentDetailsDTO>builder()
                .payload(response)
                .build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<APIResponseDTO<UUID>> addBusinessDocument(@RequestHeader("UserId") UUID userId,
                                                                    @RequestParam MultipartFile document,
                                                                    @RequestParam UUID businessDocumentTypeId)
            throws DetailsNotFoundException, IOException {


        MsmeBusinessDocument saveDocument = businessDocumentService.saveDocument(document, businessDocumentTypeId, userId);


        return new ResponseEntity<>(APIResponseDTO.<UUID>builder()
                .payload(saveDocument.getBusinessDocumentId())
                .build(), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<APIResponseDTO<String>> updateBusinessDocument(@RequestHeader("UserId") UUID userId,
                                                                         @RequestParam UUID businessDocumentId,
                                                                         @RequestParam MultipartFile document) throws IOException {


        businessDocumentService.updateDocument(document, businessDocumentId, userId);

        return new ResponseEntity<>(APIResponseDTO.<String>builder()
                .payload("Document updated successfully")
                .build(), HttpStatus.OK);

    }
}
