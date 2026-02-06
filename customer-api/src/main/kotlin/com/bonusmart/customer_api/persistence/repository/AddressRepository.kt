package com.bonusmart.customer_api.persistence.repository

import com.bonusmart.customer_api.persistence.entity.Address
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AddressRepository : JpaRepository<Address, UUID> {
    fun findByCustomerId(customerId: UUID): List<Address>
}