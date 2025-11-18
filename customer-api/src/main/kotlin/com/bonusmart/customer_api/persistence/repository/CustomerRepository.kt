package com.bonusmart.customer_api.persistence.repository

import com.bonusmart.customer_api.persistence.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerRepository : JpaRepository<Customer, UUID>