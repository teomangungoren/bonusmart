package com.bonusmart.auth_api.controller;

import com.bonusmart.auth_api.model.request.CreateCustomerRequest;
import com.bonusmart.auth_api.model.request.LoginUserRequest;
import com.bonusmart.auth_api.model.response.LoginResponse;
import com.bonusmart.auth_api.persistence.entity.User;
import com.bonusmart.auth_api.service.UserApplicationService;
import com.bonusmart.auth_api.service.UserAuthService;
import com.bonusmart.auth_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserAuthService userAuthService;
    private final UserApplicationService userService;


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody CreateCustomerRequest request) {
        return new ResponseEntity(userService.createUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserRequest request){
        return new ResponseEntity(userAuthService.login(request), HttpStatus.OK);
    }

}
