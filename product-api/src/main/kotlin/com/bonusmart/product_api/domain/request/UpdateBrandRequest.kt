package com.bonusmart.product_api.domain.request

data class UpdateBrandRequest(
    val name: String? = null,
    val description: String? = null,
    val slug: String? = null,
    val logoUrl: String? = null,
    val isActive: Boolean? = null
)



