package com.bonusmart.auth_api.config;

import com.bonusmart.auth_api.model.dto.JwtAuthenticationToken;
import com.bonusmart.auth_api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();
        String username = authentication.getPrincipal().toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!tokenService.validateToken(token, userDetails)) {
            throw new BadCredentialsException("Invalid token");
        }
        return new JwtAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
     return  UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
