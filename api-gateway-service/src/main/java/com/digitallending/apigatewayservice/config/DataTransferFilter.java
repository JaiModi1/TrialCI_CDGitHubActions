package com.digitallending.apigatewayservice.config;

import com.digitallending.apigatewayservice.exception.UnableToParseTokenException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class DataTransferFilter extends AbstractGatewayFilterFactory<DataTransferFilter.Config> {

    public DataTransferFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Extract the access token from the original request headers
            String accessToken = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (accessToken != null) {
                accessToken = accessToken.substring(7);
                try {
                    JWTClaimsSet jwtClaimsSet = JWTParser.parse(accessToken).getJWTClaimsSet();

                    String userId = (String) jwtClaimsSet.getClaim("preferred_username");
                    String rolesClaim = jwtClaimsSet.getJSONObjectClaim("realm_access").get("roles").toString();
                    String role = rolesClaim.replaceAll("[\\[\\]\"]", "").split(",")[0];

                    exchange.getRequest().mutate()
                            .header("Role", role)
                            .build();
                    exchange.getRequest().mutate()
                            .header("UserId", userId)
                            .build();
                } catch (ParseException e) {
                    throw new UnableToParseTokenException("Unable to parse token :" + accessToken);
                }
            }

            return chain.filter(exchange);
        };
    }

    @Override
    public String name() {
        return "DataTransferFilter";
    }

    public static class Config {

    }

}
