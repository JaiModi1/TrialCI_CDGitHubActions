package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.exception.DocumentTypeNotFoundException;
import com.digitallending.userservice.exception.EmptyDocumentException;
import com.digitallending.userservice.exception.InvalidUserException;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.model.dto.business.BusinessDocumentDTO;
import com.digitallending.userservice.model.dto.business.BusinessDocumentDetailsDTO;
import com.digitallending.userservice.model.dto.business.BusinessDocumentTypeDTO;
import com.digitallending.userservice.model.dto.business.BusinessTypeDTO;
import com.digitallending.userservice.model.entity.BusinessDocumentType;
import com.digitallending.userservice.model.entity.msmebusiness.BusinessType;
import com.digitallending.userservice.model.entity.msmebusiness.MsmeBusinessDocument;
import com.digitallending.userservice.model.mapper.BusinessDocumentMapper;
import com.digitallending.userservice.model.mapper.BusinessTypeMapper;
import com.digitallending.userservice.repository.BusinessDocumentRepository;
import com.digitallending.userservice.repository.BusinessDocumentTypeRepository;
import com.digitallending.userservice.repository.BusinessTypeRepository;
import com.digitallending.userservice.service.def.BusinessDetailsService;
import com.digitallending.userservice.service.def.BusinessDocumentService;
import com.digitallending.userservice.service.def.BusinessTypeService;
import com.digitallending.userservice.service.def.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BusinessDocumentServiceImpl implements BusinessTypeService, BusinessDocumentService {

    private final String businessDocumentType = "Partner ID Proof";
    @Autowired
    private BusinessDocumentRepository businessDocumentRepo;
    @Autowired
    private BusinessTypeRepository businessTypeRepository;
    @Autowired
    private BusinessDocumentTypeRepository businessDocumentTypeRepository;
    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;
    @Autowired
    private BusinessDocumentMapper businessDocumentMapper;

    @Autowired
    @Lazy
    private BusinessDetailsService businessDetailsService;
    @Autowired
    private BusinessTypeMapper businessTypeMapper;
    private UUID partnerIdBusinessDocumentTypeId;


    @Override
    public List<BusinessDocumentTypeDTO> getRemainingDocuments(UUID userId, UUID businessTypeId) {
        List<BusinessDocumentTypeDTO> remainingDocumentList = new ArrayList<>();
        List<BusinessDocumentTypeDTO> listOfReqDoc = getDocumentTypes(businessTypeId);
        List<BusinessDocumentTypeDTO> listOfSubmittedDocs = getDocTypeOfSubmittedDocs(userId);
        listOfReqDoc.forEach(docType -> {
            if (!listOfSubmittedDocs.contains(docType)) {
                remainingDocumentList.add(docType);
            }
        });

        if (listOfReqDoc.size() >= 4 && !remainingDocumentList.contains(BusinessDocumentTypeDTO
                .builder()
                .businessDocumentType(businessDocumentType)
                .businessDocumentTypeId(partnerIdBusinessDocumentTypeId)
                .build())) {
            remainingDocumentList.add(BusinessDocumentTypeDTO
                    .builder()
                    .businessDocumentType(businessDocumentType)
                    .businessDocumentTypeId(partnerIdBusinessDocumentTypeId)
                    .build());

        }
        return remainingDocumentList;
    }

    public List<BusinessDocumentTypeDTO> getDocTypeOfSubmittedDocs(UUID userId) {
        List<BusinessDocumentDTO> listOfSubmittedDocs = getDocumentsByUserId(userId);
        List<BusinessDocumentTypeDTO> listOfBusinessDocumentTypeDTO = new ArrayList<>();
        listOfSubmittedDocs.forEach(document -> listOfBusinessDocumentTypeDTO.add(document.getBusinessDocumentType()));

        return listOfBusinessDocumentTypeDTO;
    }


    @Override
    @Transactional
    public List<BusinessDocumentDTO> getDocumentsByUserId(UUID userId) {
        List<MsmeBusinessDocument> listOfBusinessDocument = businessDocumentRepo.findByUserUserId(userId);
        List<BusinessDocumentDTO> listOfDoc = new ArrayList<>();
        for (MsmeBusinessDocument document : listOfBusinessDocument) {

            listOfDoc.add(BusinessDocumentDTO
                    .builder()
                    .businessDocumentId(document.getBusinessDocumentId())
                    .documentContent(document.getDocumentContent())
                    .businessDocumentType(BusinessDocumentTypeDTO
                            .builder()
                            .businessDocumentTypeId(document.getBusinessDocumentType().getBusinessDocumentTypeId())
                            .businessDocumentType(document.getBusinessDocumentType().getBusinessDocumentType())
                            .build())
                    .build());
        }

        return listOfDoc;
    }


    @Override
    @Transactional
    public MsmeBusinessDocument saveDocument(MultipartFile document, UUID documentTypeId, UUID userId) throws DetailsNotFoundException, IOException {

        if(document.isEmpty()){
            throw new EmptyDocumentException("Document is empty");
        }
        BusinessType businessType = businessDetailsService.getBusinessTypeByUserId(userId);
        UserOnBoardingStatus onBoardingStatus = userDetailsService.getUserStatus(userId);
        if (onBoardingStatus.compareTo(UserOnBoardingStatus.BUSINESS_DETAILS) < 0) {
            throw new InvalidUserException("Please finish the steps before this");
            //done
        }
        else if(onBoardingStatus.compareTo(UserOnBoardingStatus.REVERIFY) >=0){
            throw new InvalidUserException("Can not save");
        }

        Optional<BusinessDocumentType> documentTypeOpt = businessDocumentTypeRepository.findById(documentTypeId);
        if (documentTypeOpt.isEmpty()) {
            throw new DetailsNotFoundException("Can not find this document type");
        }
        BusinessDocumentType documentType = documentTypeOpt.get();

        if (!documentType.getBusinessDocumentType().equals(businessDocumentType) && businessDocumentRepo.findByUserUserIdAndBusinessDocumentTypeBusinessDocumentTypeId(userId, documentType.getBusinessDocumentTypeId()) != null) {
            throw new InvalidUserException("Can not make 2 entries of the same document type");
        }
        if (!businessType.getDocumentTypeList().contains(documentType)) {
            throw new DocumentTypeNotFoundException("The document is not required as per your business type");
        }

        MsmeBusinessDocument businessDocument = businessDocumentRepo.save(MsmeBusinessDocument.builder()
                .documentContent(document.getBytes())
                .user(userDetailsService.getUserDetails(userId))
                .businessDocumentType(documentType)
                .build());

        List<BusinessDocumentTypeDTO> listOfRemainingDocs = getRemainingDocuments(userId, businessType.getBusinessTypeId());

        if ((listOfRemainingDocs.isEmpty() && onBoardingStatus.equals(UserOnBoardingStatus.BUSINESS_DETAILS)) ||
                (listOfRemainingDocs.size() == 1 &&
                        businessDocumentRepo.countByUserUserId(userId) >= 5) && onBoardingStatus.equals(UserOnBoardingStatus.BUSINESS_DETAILS) ) {


            userDetailsService.updateUserStatus(userId, UserOnBoardingStatus.BUSINESS_DOC);
        }
        return businessDocument;
    }

    @Override
    @Transactional
    public void changeBusinessType(BusinessType oldType, BusinessType newType, UUID userId) {

        List<BusinessDocumentTypeDTO> listOfOldReqDocs = getDocumentTypes(oldType.getBusinessTypeId());
        List<BusinessDocumentTypeDTO> listOfNewReqDocs = getDocumentTypes(newType.getBusinessTypeId());

        if (listOfNewReqDocs.size() == listOfOldReqDocs.size()) {
            System.out.println("Yaha aaiya tha");
            return;
        }
        
        List<BusinessDocumentTypeDTO> listOfSubmittedDocs = getDocTypeOfSubmittedDocs(userId);

        listOfSubmittedDocs.forEach(docType -> {
            if (!listOfNewReqDocs.contains(docType)) {
                businessDocumentRepo.deleteByUserUserIdAndBusinessDocumentTypeBusinessDocumentTypeId(userId, docType.getBusinessDocumentTypeId());
            }
        });
        listOfSubmittedDocs = getDocTypeOfSubmittedDocs(userId);
        if (listOfSubmittedDocs.size() == listOfNewReqDocs.size() && userDetailsService.getUserStatus(userId).equals(UserOnBoardingStatus.BUSINESS_DETAILS)) {
            userDetailsService.updateUserStatus(userId, UserOnBoardingStatus.BUSINESS_DOC);
        }

    }


    @Override
    @Transactional
    public MsmeBusinessDocument getDocumentByBusinessDocumentId(UUID businessDocumentId) {
        return businessDocumentRepo.findById(businessDocumentId).orElse(null);
    }


    @Override
    @Transactional
    public MsmeBusinessDocument updateDocument(MultipartFile document, UUID businessDocumentId, UUID userId) throws IOException {

        if(document.isEmpty()){
            throw new EmptyDocumentException("Document is empty");
        }
        UserOnBoardingStatus onBoardingStatus = userDetailsService.getUserStatus(userId);
        if (onBoardingStatus.compareTo(UserOnBoardingStatus.BUSINESS_DETAILS) < 0 || onBoardingStatus.compareTo(UserOnBoardingStatus.ON_HOLD) > 0) {
            throw new UpdateException("This user can not update details");
        }

        MsmeBusinessDocument businessDoc = getDocumentByBusinessDocumentId(businessDocumentId);
        if(businessDoc == null){
            throw new DetailsNotFoundException("Can not locate this document");
        }
        if (!businessDoc.getUser().getUserId().equals(userId)) {
            throw new InvalidUserException("This row doesn't belong to the provided user");
        }

        businessDoc.setDocumentContent(document.getBytes());
        return businessDocumentRepo.save(businessDoc);
    }


    /*
     * From the BusinessTypeService
     * */
    @Override
    public List<BusinessDocumentTypeDTO> getDocumentTypes(UUID businessTypeId) {
        Optional<BusinessType> businessType = businessTypeRepository.findById(businessTypeId);
        if (businessType.isEmpty()) {
            return new ArrayList<>();
        }
        List<BusinessDocumentType> listOfDocuments = businessType.get().getDocumentTypeList();

        List<BusinessDocumentTypeDTO> documentTypeList = new ArrayList<>();

        listOfDocuments.forEach(docType ->
                documentTypeList.add(BusinessDocumentTypeDTO
                        .builder()
                        .businessDocumentTypeId(docType.getBusinessDocumentTypeId())
                        .businessDocumentType(docType.getBusinessDocumentType())
                        .build())
        );

        return documentTypeList;

    }

    @Override
    public BusinessType getBusinessTypeByBusinessTypeId(UUID businessTypeId) {
        Optional<BusinessType> businessType = businessTypeRepository.findById(businessTypeId);
        if (businessType.isEmpty()) {
            return null;
        }
        return businessType.get();
    }


    @Override
    public boolean isBusinessTypePresent(BusinessType businessType) {
        BusinessType business = businessTypeRepository.findById(businessType.getBusinessTypeId()).orElse(null);
        return business != null && business.getBusinessType().equals(businessType.getBusinessType());
    }

    @Override
    public List<BusinessTypeDTO> getAllBusinessTypes() {
        return businessTypeMapper.toListResponse(businessTypeRepository.findAll());
    }

    /*
     * DB initialization
     * */

    @Override
    @Transactional
    public void initializeDatabase() {
        if (businessDocumentTypeRepository.findByBusinessDocumentType("Business Registration Document") == null) {

            BusinessDocumentType registrationDoc = businessDocumentTypeRepository.save(BusinessDocumentType
                    .builder()
                    .businessDocumentType("Business Registration Document")
                    .build());
            BusinessDocumentType address = businessDocumentTypeRepository.save(BusinessDocumentType
                    .builder()
                    .businessDocumentType("Address Proof")
                    .build());
            BusinessDocumentType shareholding = businessDocumentTypeRepository.save(BusinessDocumentType
                    .builder()
                    .businessDocumentType("Shareholder pattern")
                    .build());
            BusinessDocumentType partner = businessDocumentTypeRepository.save(BusinessDocumentType
                    .builder()
                    .businessDocumentType("Partner ID Proof")
                    .build());

            businessTypeRepository.save(BusinessType
                    .builder()
                    .businessType("Sole Proprietorship")
                    .documentTypeList(Arrays.asList(registrationDoc, address))
                    .build());
            businessTypeRepository.save(BusinessType
                    .builder()
                    .businessType("Private Limited Company")
                    .documentTypeList(Arrays.asList(registrationDoc, address, shareholding, partner))
                    .build());
            businessTypeRepository.save(BusinessType
                    .builder()
                    .businessType("Public Limited Company")
                    .documentTypeList(Arrays.asList(registrationDoc, address, shareholding, partner))
                    .build());
            businessTypeRepository.save(BusinessType
                    .builder()
                    .businessType("Partnership")
                    .documentTypeList(Arrays.asList(registrationDoc, address, shareholding, partner))
                    .build());

        }
        partnerIdBusinessDocumentTypeId = businessDocumentTypeRepository.findByBusinessDocumentType(businessDocumentType).getBusinessDocumentTypeId();

    }

    @Override
    @Transactional
    public BusinessDocumentDetailsDTO getBusinessDocumentDetailsByUserId(UUID userId) {
        List<BusinessDocumentDTO> listOfSubmittedDocs = getDocumentsByUserId(userId);
        BusinessType businessType = businessDetailsService.getBusinessTypeByUserId(userId);
        List<BusinessDocumentTypeDTO> listOfRemainingDocs = getRemainingDocuments(userId, businessType.getBusinessTypeId());
        return BusinessDocumentDetailsDTO
                .builder()
                .listOfSubmittedDocs(listOfSubmittedDocs)
                .listOfRemainingDocs(listOfRemainingDocs)
                .build();
    }


}
