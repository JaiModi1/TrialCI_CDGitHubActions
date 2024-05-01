package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.model.entity.msmeuser.UserDocumentType;
import com.digitallending.userservice.repository.MsmeUserDocumentTypeRepository;
import com.digitallending.userservice.service.def.MsmeUserDocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsmeUserDocumentTypeServiceImpl implements MsmeUserDocumentTypeService {

    @Autowired
    private MsmeUserDocumentTypeRepository msmeUserDocumentTypeRepository;


    @Override
    public List<UserDocumentType> getAllDocuments() {
        return msmeUserDocumentTypeRepository.findAll();
    }
}
