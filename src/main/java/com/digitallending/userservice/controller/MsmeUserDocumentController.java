package com.digitallending.userservice.controller;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.model.dto.apiresponse.APIResponseDTO;
import com.digitallending.userservice.model.dto.userdetails.MsmeUserDocumentDTO;
import com.digitallending.userservice.model.dto.userdetails.UserDocumentDetailsDTO;
import com.digitallending.userservice.service.def.MsmeUserDocumentService;
import com.digitallending.userservice.service.def.UserDetailsService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/user/msme-document")
public class MsmeUserDocumentController {

    @Autowired
    private MsmeUserDocumentService msmeUserDocumentService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/")
    public ResponseEntity<APIResponseDTO<MsmeUserDocumentDTO>> saveUserDocument(@NotNull @RequestParam MultipartFile document, @NotNull @RequestParam UUID documentTypeId, @RequestHeader("UserId") UUID userId) {
        UserOnBoardingStatus onBoardingStatus = userDetailsService.getUserStatus(userId);
        if (onBoardingStatus.compareTo(UserOnBoardingStatus.USER_DETAILS) < 0 || onBoardingStatus.compareTo(UserOnBoardingStatus.ON_HOLD) > 0) {
            throw new UpdateException("This user can not update details");
        }
        MsmeUserDocumentDTO savedDocument = msmeUserDocumentService.updateDocument(userId, documentTypeId, document);
        APIResponseDTO<MsmeUserDocumentDTO> apiResponseDTO = APIResponseDTO.<MsmeUserDocumentDTO>builder()
                .payload(savedDocument)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<APIResponseDTO<UserDocumentDetailsDTO>> getAllDocumentsByUserId(@RequestHeader("UserId") UUID userId) {
        UserDocumentDetailsDTO documents = msmeUserDocumentService.getAllDocumentByUserId(userId);
        APIResponseDTO<UserDocumentDetailsDTO> apiResponseDTO = APIResponseDTO.<UserDocumentDetailsDTO>builder()
                .payload(documents)
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }


}
