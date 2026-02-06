package com.bonusmart.customer_api.model.request

import com.bonusmart.customer_api.persistence.entity.Address
import com.bonusmart.customer_api.persistence.entity.Customer

data class CreateAddressRequest(
    val title: String,
    val recipientName: String,
    val phoneNumber: String?,
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val province: String,
    val postalCode: String,
    val country: String,
    val isDefault : Boolean = false
) {
    companion object {
        fun CreateAddressRequest.toAddress(customer: Customer): Address {
            return Address(
                title = this.title,
                addressLine1 = this.addressLine1,
                recipientName = this.recipientName,
                phoneNumber = this.phoneNumber,
                addressLine2 = this.addressLine2,
                city = this.city,
                province = this.province,
                postalCode = this.postalCode,
                country = this.country,
                isDefault = this.isDefault,
                customer = customer
            )
        }
    }
}
