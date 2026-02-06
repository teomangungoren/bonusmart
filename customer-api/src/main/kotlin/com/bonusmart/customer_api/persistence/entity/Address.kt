package com.bonusmart.customer_api.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "addresses")
data class Address(
    @GeneratedValue
    @Id
    val id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    val customer: Customer,
    var title: String,
    var recipientName: String,
    var phoneNumber: String? = null,
    var addressLine1: String,
    var addressLine2: String,
    var city: String,
    var province: String,
    var postalCode: String,
    var country: String,
    var deleted: Boolean = false,
    var isDefault: Boolean = false

)
