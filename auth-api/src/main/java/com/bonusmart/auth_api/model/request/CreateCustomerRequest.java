package com.bonusmart.auth_api.model.request;

import com.bonusmart.auth_api.model.enums.Role;
import com.bonusmart.auth_api.persistence.entity.User;

import java.time.LocalDate;
import java.util.Set;

public record CreateCustomerRequest(
        String firstName,
        String lastName,
        String username,
        String email,
        String password,
        LocalDate dateOfBirth
) {

    public static User toUser(CreateCustomerRequest request) {
        return User.builder()
                .username(request.username())
                .password(request.password())
                .roles(Set.of(Role.USER))
                .build();
    }
}
