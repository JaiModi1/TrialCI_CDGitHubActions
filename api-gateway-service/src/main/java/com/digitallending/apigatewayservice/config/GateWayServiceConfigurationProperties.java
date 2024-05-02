package com.digitallending.apigatewayservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "gatewayservice")
public class GateWayServiceConfigurationProperties {
    private String issuer;
}
