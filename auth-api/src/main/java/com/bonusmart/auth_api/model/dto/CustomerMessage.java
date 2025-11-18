package com.bonusmart.auth_api.model.dto;

import com.bonusmart.auth_api.model.request.CreateCustomerRequest;

import java.time.LocalDate;

public record CustomerMessage(
        String firstName,
        String lastName,
        String username,
        String email,
        String dateOfBirth
) {

    public static CustomerMessage toCustomerMessage(CreateCustomerRequest request) {
        return new CustomerMessage(
                request.firstName(),
                request.lastName(),
                request.username(),
                request.email(),
                request.dateOfBirth().toString()
        );
    }
}
