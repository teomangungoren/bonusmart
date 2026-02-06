package com.bonusmart.auth_api.config;

import com.bonusmart.auth_api.model.dto.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final ProviderManager providerManager;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        String token = authHeader.replace("Bearer ", "");
        var authenticationResult = providerManager.authenticate(new JwtAuthenticationToken(token));
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
        filterChain.doFilter(request, response);
    }
}
