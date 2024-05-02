package com.digitallending.apigatewayservice.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenAuthenticationConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class ReactiveOpaqueTokenAuthConverter implements ReactiveOpaqueTokenAuthenticationConverter {

    @Autowired
    private GateWayServiceConfigurationProperties gateWayServiceConfigurationProperties;

    @Override
    public Mono<Authentication> convert(String introspectedToken, OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
        Collection<GrantedAuthority> roles = extractAuthorities(authenticatedPrincipal);
        JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(gateWayServiceConfigurationProperties.getIssuer());
        Jwt token = jwtDecoder.decode(introspectedToken);
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token, roles);
        return Mono.just(jwtAuthenticationToken);

    }

    private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal authenticatedPrincipal) {

        Map<String, Object> realmAccess = authenticatedPrincipal.getAttribute("realm_access");
        if (realmAccess == null) {
            return new ArrayList<>();
        }
        ObjectMapper mapper = new ObjectMapper();
        List<String> keycloakRoles = mapper.convertValue(realmAccess.get("roles"), new TypeReference<List<String>>() {
        });
        ArrayList<GrantedAuthority> roles = new ArrayList<>();
        for (String keycloakRole : keycloakRoles) {
            roles.add(new SimpleGrantedAuthority(keycloakRole));
        }
        return roles;
    }
}
