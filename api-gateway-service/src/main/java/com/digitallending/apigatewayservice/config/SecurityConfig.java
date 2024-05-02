package com.digitallending.apigatewayservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String MSME = "MSME";
    private static final String LENDER = "LENDER";
    private static final String ADMIN = "ADMIN";

    @Autowired
    private ReactiveOpaqueTokenAuthenticationConverter reactiveOpaqueTokenAuthenticationConverter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {

        String[] POST_KYC = List.of(
                        "/api/user/generate-mobile-otp",
                        "/api/user/verfiy-mobile-otp",
                        "/api/user/verify-pan",
                        "/api/user/generate-aadhaar-otp",
                        "/api/user/verify-aadhaar-otp")
                .toArray(new String[5]);

        String[] GET_MSME = List.of(
                "/api/user/msme-details",
                        "/api/user/msme-details/attribute-values",
                        "/api/user/msme-documents/",
                        "/api/user/business-details",
                        "/api/user/business-document",
                        "api/loan/loan-product/applicable/{loanApplicationId}",
                        "/api/user/business-details/business-types",
                        "/api/loan/loan-application/document/remaining",
                        "/api/loan/loan-application/document/submit",
                        "api/loan/loan-request/loan-application/{loanApplicationId}/new-request-allowed",
                        "/api/loan/loan-application/{loanApplicationId}/sign-document")
                .toArray(new String[11]);

        String[] POST_MSME = List.of(
                        "/api/loan/loan-application",
                        "/api/loan/loan-request",
                        "/api/loan/property-details",
                        "/api/user/msme-details",
                        "/api/user/business-details",
                        "/api/user/msme-documents/",
                        "/api/user/business-document",
                        "/api/loan/loan-application/document")
                .toArray(new String[8]);

        String[] PUT_MSME = List.of(
                        "/api/user/msme-details",
                        "/api/user/business-details")
                .toArray(new String[2]);

        String[] PATCH_MSME = List.of(
                        "/api/user/business-document",
                        "api/loan/loan-application/submit",
                        "/api/loan/loan-request/accept")
                .toArray(new String[3]);

        String[] PATCH_LENDER = List.of(
                        "/api/loan/loan-request/offered")
                .toArray(new String[1]);


        String[] GET_ADMIN = List.of(
                        "/api/loan/loan-statistics/admin",
                        "/api/loan/transaction",
                        "/api/loan/signed-document/unapproved-signed-document",
                        "/api/user/chart",
                        "/api/bre/get-param-types/{loanTypeId}",
                        "/api/bre/get-sub-params/{loanProductId}",
                        "/api/bre/get-sub-params-for-sanction-condition/{loanTypeId}",
                        "/api/user/filter-types",
                        "/api/user/filter"
                )
                .toArray(new String[9]);

        String[] GET_LENDER = List.of(
                        "/api/loan/loan-statistics/lender")
                .toArray(new String[1]);

        String[] POST_ADMIN = List.of(
                        "/api/user/full-profile",
                        "/api/loan/loan-product",
                        "api/bre/save-param-weights",
                        "api/bre/save-sub-param-weights",
                        "api/bre/save-sanction-condition-range",
                        "api/bre/save-sanction-condition-value",
                        "api/bre/save-score-matrix"
                )
                .toArray(new String[7]);

        String[] POST_LENDER = List.of(
                        "/api/loan/transaction/submit",
                        "/api/loan/transaction/initiate")
                .toArray(new String[2]);

        String[] PATCH_ADMIN = List.of(
                        "/api/loan/loan-application/status",
                        "/api/loan/signed-document/change-status",
                        "/api/user/change-status")
                .toArray(new String[3]);

        String[] GET_MSME_LENDER = List.of(
                        "/api/user/bank-account",
                        "/api/user/apply",
                        "/api/loan/loan-type",
                        "/api/user/generate-email-otp",
                        "/api/user/resend-email-otp",
                        "/api/user/profile")
                .toArray(new String[6]);

        String[] POST_MSME_LENDER = List.of(
                        "/api/user/bank-account",
                        "/api/user/verify-email-otp",
                        "/api/user/initiate-update-email",
                        "/api/user/update-email",
                        "/api/user/change-password",
                        "/api/user/detail-profile")
                .toArray(new String[6]);

        String[] PUT_MSME_LENDER = List.of(
                        "/api/user/bank-account/{bankAccountId}")
                .toArray(new String[1]);

        String[] PATCH_MSME_LENDER = List.of(
                        "/api/loan/loan-request/change-status",
                        "/api/loan/signed-document/upload-signed-document")
                .toArray(new String[2]);

        String[] DELETE_MSME_LENDER = List.of(
                        "/api/user/bank-account/{bankAccountId}")
                .toArray(new String[1]);

        String[] GET_ADMIN_MSME = List.of(
                        "/api/loan/loan-application/{loanApplicationId}",
                        "/api/loan/loan-application/status/{loanApplicationStatus}",
                        "/api/loan/loan-application/user",
                        "/api/loan/transaction/loan-application/{loanApplicationId}",
                        "/api/loan/loan-request/loan-application/{loanApplicationId}",
                        "/api/loan/loan-application/document")
                .toArray(new String[6]);

        String[] GET_ADMIN_LENDER = List.of(
                        "/api/loan/loan-application/loan-request/{loanRequestId}",
                        "/api/loan/loan-product/{productId}",
                        "/api/loan/loan-product",
                        "/api/loan/transaction/loan-product/{loanProductId}",
                        "/api/loan/loan-request/loan-product/{loanProductId}",
                        "/api/loan/loan-application/document/loan-request")
                .toArray(new String[6]);

        String[] GET_ADMIN_MSME_LENDER = List.of(
                        "/api/loan/property-details/property-type",
                        "/api/loan/transaction/user",
                        "/api/user/sign-out")
                .toArray(new String[3]);

        String[] POST_OPEN = List.of(
                        "/api/user/sign-in",
                        "/api/user/sign-up",
                        "/api/user/forgot-password",
                        "/api/user/confirm-otp")
                .toArray(new String[4]);
        String[] PATCH_OPEN = List.of("/api/user/reset-password").toArray(new String[1]);

        String[] GET_OPEN = List.of(
                        "/api/user/regenerate-token",
                        "/api/loan/loan-request/details")
                .toArray(new String[1]);

        String[] GET_RESTRICTED = List.of(
                        "/api/user/check-user",
                        "/api/user/bank-account/link-status",
                        "/api/user/bank-account/{bankAccountId}",
                        "/api/user/{userId}/bre-details",
                        "/api/user/{userId}/role",
                        "/api/user/{userId}/name",
                        "/api/loan/loan-product/{loanProductId}/loan-type-id",
                        "api/bre/run")
                .toArray(new String[8]);

        String[] POST_RESTRICTED = List.of(
                        "/api/user/send-mail",
                        "/api/user/transaction-otp",
                        "/api/user/verify-transaction-otp",
                        "/api/user/")
                .toArray(new String[4]);

        return serverHttpSecurity

                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource))

                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(exchange -> exchange

                        .pathMatchers(HttpMethod.GET, GET_ADMIN_MSME).hasAnyAuthority(ADMIN, MSME)

                        .pathMatchers(HttpMethod.PATCH, PATCH_ADMIN).hasAuthority(ADMIN)

                        .pathMatchers(HttpMethod.GET, GET_ADMIN).hasAuthority(ADMIN)

                        .pathMatchers(HttpMethod.POST, POST_ADMIN).hasAuthority(ADMIN)

                        .pathMatchers(HttpMethod.PATCH, PATCH_LENDER).hasAuthority(LENDER)

                        .pathMatchers(HttpMethod.GET, GET_LENDER).hasAuthority(LENDER)

                        .pathMatchers(HttpMethod.POST, POST_LENDER).hasAuthority(LENDER)

                        .pathMatchers(HttpMethod.POST, POST_MSME).hasAuthority(MSME)

                        .pathMatchers(HttpMethod.PATCH, PATCH_MSME).hasAuthority(MSME)

                        .pathMatchers(HttpMethod.PUT, PUT_MSME).hasAuthority(MSME)

                        .pathMatchers(HttpMethod.GET, GET_MSME).hasAuthority(MSME)

                        .pathMatchers(HttpMethod.POST, POST_KYC).hasAnyAuthority(MSME, LENDER)

                        .pathMatchers(HttpMethod.GET, GET_MSME_LENDER).hasAnyAuthority(MSME, LENDER)

                        .pathMatchers(HttpMethod.POST, POST_MSME_LENDER).hasAnyAuthority(MSME, LENDER)

                        .pathMatchers(HttpMethod.PATCH, PATCH_MSME_LENDER).hasAnyAuthority(MSME, LENDER)

                        .pathMatchers(HttpMethod.PUT, PUT_MSME_LENDER).hasAnyAuthority(MSME, LENDER)

                        .pathMatchers(HttpMethod.DELETE, DELETE_MSME_LENDER).hasAnyAuthority(MSME, LENDER)

                        .pathMatchers(HttpMethod.GET, GET_ADMIN_LENDER).hasAnyAuthority(LENDER, ADMIN)

                        .pathMatchers(HttpMethod.GET, GET_ADMIN_MSME_LENDER).hasAnyAuthority(ADMIN, LENDER, MSME)

                        .pathMatchers(HttpMethod.POST, POST_OPEN).permitAll()

                        .pathMatchers(HttpMethod.GET, GET_OPEN).permitAll()
                        .pathMatchers(HttpMethod.PATCH, PATCH_OPEN).permitAll()

                        .pathMatchers(HttpMethod.GET, GET_RESTRICTED).denyAll()

                        .pathMatchers(HttpMethod.POST, POST_RESTRICTED).denyAll()

                        .anyExchange().authenticated()

                )
                .oauth2ResourceServer(
                        oauth -> oauth.opaqueToken(
                                opaqueTokenSpec -> opaqueTokenSpec.authenticationConverter(
                                        reactiveOpaqueTokenAuthenticationConverter)))
                .build();

    }

}