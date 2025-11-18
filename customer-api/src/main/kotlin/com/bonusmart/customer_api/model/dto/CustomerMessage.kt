package com.bonusmart.customer_api.model.dto

import com.bonusmart.customer_api.persistence.entity.Customer
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CustomerMessage(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val dateOfBirth: String,
) {
    companion object {
        fun toCustomer(customerMessage: CustomerMessage): Customer {

            return Customer(
                firstName = customerMessage.firstName,
                lastName = customerMessage.lastName,
                username = customerMessage.username,
                email = customerMessage.email,
                dateOfBirth = LocalDate.parse(customerMessage.dateOfBirth)
            )
        }
    }
}
