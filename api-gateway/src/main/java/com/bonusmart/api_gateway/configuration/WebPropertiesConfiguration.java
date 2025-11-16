package com.bonusmart.api_gateway.configuration;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebPropertiesConfiguration {

    @Bean
    public WebProperties.Resources webProperties() {
        return new WebProperties.Resources();
    }
}
