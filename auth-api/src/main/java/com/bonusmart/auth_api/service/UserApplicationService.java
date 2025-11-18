package com.bonusmart.auth_api.service;

import com.bonusmart.auth_api.model.dto.CustomerMessage;
import com.bonusmart.auth_api.model.request.CreateCustomerRequest;
import com.bonusmart.auth_api.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final KafkaNotifyService kafkaNotifyService;


    @Transactional
    public User createUser(CreateCustomerRequest request) {
        User user = CreateCustomerRequest.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser =  userService.create(user);
        kafkaNotifyService.notifyCustomer(CustomerMessage.toCustomerMessage(request));
        return savedUser;
    }



}
