package com.bonusmart.customer_api.service

import com.bonusmart.customer_api.persistence.entity.Customer
import com.bonusmart.customer_api.persistence.repository.CustomerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerService(private val customerRepository: CustomerRepository) {


    fun create(customer: Customer): Customer {
        return customerRepository.save(customer)
    }


    fun getById(id: UUID): Customer? {
        return customerRepository.findByIdOrNull(id) ?: throw RuntimeException("Customer with id $id does not exist")
    }
}