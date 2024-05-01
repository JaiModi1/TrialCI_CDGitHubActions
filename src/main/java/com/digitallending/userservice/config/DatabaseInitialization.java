package com.digitallending.userservice.config;

import com.digitallending.userservice.service.def.BusinessDocumentService;
import com.digitallending.userservice.service.def.MsmeUserDetailsService;
import com.digitallending.userservice.service.def.MsmeUserDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitialization implements CommandLineRunner {

    @Autowired
    private BusinessDocumentService businessDocumentService;

    @Autowired
    private MsmeUserDocumentService msmeUserDocumentService;

    @Autowired
    private MsmeUserDetailsService msmeUserDetailsService;

    @Override
    public void run(String... args) throws Exception {
        businessDocumentService.initializeDatabase();
        msmeUserDocumentService.initializeDatabase();
        msmeUserDetailsService.initializeDatabase();
        System.out.println("abcd");
    }

}
