package com.bonusmart.product_api.domain.request

data class UpdateCategoryRequest(
    val name: String? = null,
    val description: String? = null
)

