package com.bonusmart.auth_api.service;

import com.bonusmart.auth_api.model.request.LoginUserRequest;
import com.bonusmart.auth_api.model.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    public final UserDetailsService userDetailsService;
    public final TokenService tokenService;
    public final UserService userService;

    public LoginResponse login(LoginUserRequest request) {
        UserDetails user = userDetailsService.loadUserByUsername(request.username());
        userService.checkUserPassword(request.password(), user.getPassword());
        String token = tokenService.createToken(user);
        return new LoginResponse(token);
    }
}
