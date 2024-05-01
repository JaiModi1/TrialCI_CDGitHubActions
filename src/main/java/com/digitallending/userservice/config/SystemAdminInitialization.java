package com.digitallending.userservice.config;

import com.digitallending.userservice.service.def.KeyCloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SystemAdminInitialization implements CommandLineRunner {

    @Autowired
    private KeyCloakService keyCloakService;

    @Override
    public void run(String... args) throws Exception {
//        keyCloakService.createAdminUser();
    }
}
