package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.DocumentTypeNotFoundException;
import com.digitallending.userservice.exception.EmptyDocumentException;
import com.digitallending.userservice.exception.InternalServerException;
import com.digitallending.userservice.exception.PreviousStepsNotDoneException;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.model.dto.userdetails.MsmeUserDocumentDTO;
import com.digitallending.userservice.model.dto.userdetails.UserDocumentDetailsDTO;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDocument;
import com.digitallending.userservice.model.entity.msmeuser.UserDocumentType;
import com.digitallending.userservice.model.mapper.MsmeUserDocumentMapper;
import com.digitallending.userservice.repository.MsmeUserDocumentRepository;
import com.digitallending.userservice.repository.MsmeUserDocumentTypeRepository;
import com.digitallending.userservice.service.def.MsmeUserDocumentService;
import com.digitallending.userservice.service.def.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MsmeUserDocumentServiceImpl implements MsmeUserDocumentService {

    @Autowired
    private MsmeUserDocumentRepository msmeUserDocumentRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MsmeUserDocumentTypeRepository msmeUserDocumentTypeRepository;

    @Autowired
    private MsmeUserDocumentMapper msmeUserDocumentMapper;

    @Override
    @Transactional
    public void initializeDatabase() {
        long rows = msmeUserDocumentTypeRepository.count();
        if (rows == 0) {
            UserDocumentType userDocumentType = UserDocumentType
                    .builder()
                    .documentType("Category Document")
                    .documentTypeId(UUID.randomUUID())
                    .build();
            msmeUserDocumentTypeRepository.save(userDocumentType);

            userDocumentType.setDocumentType("Marital Status");
            userDocumentType.setDocumentTypeId(UUID.randomUUID());
            msmeUserDocumentTypeRepository.save(userDocumentType);

            userDocumentType.setDocumentType("Education Qualification");
            userDocumentType.setDocumentTypeId(UUID.randomUUID());
            msmeUserDocumentTypeRepository.save(userDocumentType);

        }
    }

    @Override
    @Transactional
    public MsmeUserDocumentDTO updateDocument(UUID userId, UUID documentTypeId, MultipartFile file) {

        UserDetails userDetails = userDetailsService.getUserDetails(userId);

        UserDocumentType documentType = msmeUserDocumentTypeRepository.findById(documentTypeId).orElseThrow(() -> new DocumentTypeNotFoundException("This Type Of Document Not Exist"));

        if (UserOnBoardingStatus.VERIFIED.equals(userDetails.getOnBoardingStatus())) {
            throw new UpdateException("Document Update Not Allowed");
        }
        /*
           If Status in Range Of USER To UNVERIFIED Then Update Operation is Allowed.
           Once VERIFIED Then Update Operation is Not Allowed.
        */
        if (UserOnBoardingStatus.USER_DETAILS.compareTo(userDetails.getOnBoardingStatus()) > 0) {
            throw new PreviousStepsNotDoneException("Please First Save User Details");
        }
        MsmeUserDocument msmeUserDocument = msmeUserDocumentRepository.findByUserUserIdAndDocumentTypeDocumentTypeId(userId, documentTypeId);
        if (msmeUserDocument == null) {
            msmeUserDocument = MsmeUserDocument
                    .builder()
                    .user(userDetails)
                    .documentType(documentType)
                    .build();
        }

        if (file.isEmpty()) {
            throw new EmptyDocumentException("Please Provide Document");
        }

        try {
            msmeUserDocument.setDocumentContent(file.getBytes());
        } catch (IOException exception) {
            throw new InternalServerException("Error occurred during saving file");
        }

        MsmeUserDocument savedMsmeUserDocument = msmeUserDocumentRepository.save(msmeUserDocument);

        Integer submittedDocuments = msmeUserDocumentRepository.countByUserUserId(userId);
        long noOfRequiredDocuments = msmeUserDocumentTypeRepository.count();

        if ((submittedDocuments == noOfRequiredDocuments) && UserOnBoardingStatus.USER_DETAILS.equals(userDetails.getOnBoardingStatus())) {
            userDetails.setOnBoardingStatus(UserOnBoardingStatus.USER_DOC);
            userDetailsService.updateUserDetails(userDetails);
        }
        return msmeUserDocumentMapper.toResponseDto(savedMsmeUserDocument);
    }

    @Override
    @Transactional
    public UserDocumentDetailsDTO getAllDocumentByUserId(UUID userId) {

        List<MsmeUserDocument> documentList = msmeUserDocumentRepository.findByUserUserId(userId);
        Set<UserDocumentType> documentTypes = new HashSet<>(msmeUserDocumentTypeRepository.findAll());

        documentList.forEach(document -> documentTypes.remove(document.getDocumentType()));

        return UserDocumentDetailsDTO
                .builder()
                .submittedDocuments(msmeUserDocumentMapper.toResponseDtoList(documentList))
                .remainingDocuments(documentTypes.stream().toList())
                .build();
    }


}
