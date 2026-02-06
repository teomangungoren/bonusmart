package com.bonusmart.customer_api.service

import com.bonusmart.customer_api.model.dto.CustomerMessage
import com.bonusmart.customer_api.model.request.CreateAddressRequest
import com.bonusmart.customer_api.model.request.CreateAddressRequest.Companion.toAddress
import com.bonusmart.customer_api.persistence.entity.Customer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CustomerApplicationService(
    private val customerService: CustomerService,
    private val addressService: AddressService
) {

    @Transactional
    fun createCustomer(message: CustomerMessage): Customer {
        val customer = CustomerMessage.toCustomer(message)
        return customerService.create(customer)
    }


    @Transactional
    fun addAddressToCustomer(customerId: UUID, request: CreateAddressRequest) {
        val customer = customerService.getById(customerId)
        val address = request.toAddress(customer!!)
        addressService.create(address)
    }

}