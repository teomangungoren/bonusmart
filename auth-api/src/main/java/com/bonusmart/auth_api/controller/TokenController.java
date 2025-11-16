package com.bonusmart.auth_api.controller;

import com.bonusmart.auth_api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    public final TokenService tokenService;

    @GetMapping("/validate")
    public Boolean validate(@RequestHeader("Authorization") String token) {
        return tokenService.validateToken(token);
    }
}
