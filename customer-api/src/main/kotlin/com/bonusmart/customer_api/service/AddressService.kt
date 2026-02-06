package com.bonusmart.customer_api.service

import com.bonusmart.customer_api.persistence.entity.Address
import com.bonusmart.customer_api.persistence.repository.AddressRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AddressService(
    private val addressRepository: AddressRepository
) {

    fun create(address: Address): Address {
       return addressRepository.save(address)
    }
}