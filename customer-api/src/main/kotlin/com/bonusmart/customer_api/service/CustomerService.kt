package com.bonusmart.customer_api.service

import com.bonusmart.customer_api.persistence.entity.Customer
import com.bonusmart.customer_api.persistence.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService (private val customerRepository: CustomerRepository) {


    fun create(customer: Customer): Customer {
        return customerRepository.save(customer)
    }
}