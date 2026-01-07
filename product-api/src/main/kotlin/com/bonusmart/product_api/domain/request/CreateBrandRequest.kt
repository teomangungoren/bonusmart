package com.bonusmart.product_api.domain.request

data class CreateBrandRequest(
    val name: String,
    val description: String? = null,
    val slug: String,
    val logoUrl: String? = null,
    val isActive: Boolean? = true
)



