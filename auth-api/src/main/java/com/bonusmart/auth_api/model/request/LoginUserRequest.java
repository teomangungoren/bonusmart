package com.bonusmart.auth_api.model.request;

public record LoginUserRequest(
        String username,
        String password
) {
}
