package com.bonusmart.customer_api.service

import com.bonusmart.customer_api.model.dto.CustomerMessage
import com.bonusmart.customer_api.persistence.entity.Customer
import org.springframework.stereotype.Service

@Service
class CustomerApplicationService(private val customerService: CustomerService) {

    fun createCustomer(message: CustomerMessage): Customer {
        val customer = CustomerMessage.toCustomer(message)
        return customerService.create(customer)
    }
}