package com.bonusmart.auth_api.service;

import com.bonusmart.auth_api.model.request.CreateCustomerRequest;
import com.bonusmart.auth_api.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public User createUser(CreateCustomerRequest request) {
        User user = CreateCustomerRequest.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.create(user);
    }



}
