package com.bonusmart.product_api.domain.request

import java.util.UUID

data class CreateCategoryRequest(
    val name: String,
    val description: String? = null,
    val parentId: UUID? = null
)


