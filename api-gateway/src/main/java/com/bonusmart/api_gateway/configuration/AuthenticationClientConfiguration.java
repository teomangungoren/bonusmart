package com.bonusmart.api_gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AuthenticationClientConfiguration {

    @Bean
    public WebClient webClient(AuthenticationClientProperties authenticationClientProperties) {
        return WebClient.builder()
                .baseUrl(authenticationClientProperties.getBaseUrl())
                .build();
    }
}