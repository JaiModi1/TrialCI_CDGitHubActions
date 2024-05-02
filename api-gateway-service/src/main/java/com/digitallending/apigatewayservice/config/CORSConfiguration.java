package com.digitallending.apigatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
public class CORSConfiguration {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"   // For USERS (MSME, LENDER)
                ,"http://localhost:5174"));  // FOR ADMIN

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));

        config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type","RefreshToken"));

        config.setMaxAge(1800L);                    // FOR 30 Minutes Browser Will Cache Pre-Flight Response

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}