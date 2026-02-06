package com.bonusmart.customer_api.persistence.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
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
    val dateOfBirth: LocalDate,
    var deleted: Boolean = false,

    @OneToMany(mappedBy = "customer",cascade = [CascadeType.ALL],orphanRemoval = true)
    val addresses: MutableList<Address> = mutableListOf()
)
