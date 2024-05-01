package com.digitallending.userservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "userservice")
@Component
@Data
public class UserServiceConfigurationProperties {
    private String twilioAuthToken;
    private String twilioVerifySid;
    private String twilioAccountSid;
    private String rapidApiGroupId;
    private String rapidApiKey;
    private String rapidApiTaskId;
    private String aadhaarClientId;
    private String aadhaarClientSecret;
}
