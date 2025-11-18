package com.bonusmart.customer_api.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDate
import java.util.*

@Entity
data class Customer(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val dateOfBirth: LocalDate
)
