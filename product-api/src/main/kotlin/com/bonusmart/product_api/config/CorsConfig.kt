package com.bonusmart.product_api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration().apply {
            allowCredentials = true
            addAllowedOriginPattern("http://localhost:*")
            addAllowedOriginPattern("http://127.0.0.1:*")
            addAllowedHeader("*")
            addAllowedMethod("GET")
            addAllowedMethod("POST")
            addAllowedMethod("PUT")
            addAllowedMethod("DELETE")
            addAllowedMethod("PATCH")
            addAllowedMethod("OPTIONS")
        }
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}

