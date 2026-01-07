package com.bonusmart.product_api.domain.request

import com.bonusmart.product_api.domain.enums.ProductStatus
import java.math.BigDecimal
import java.util.UUID

data class UpdateProductRequest(
    val name: String? = null,
    val description: String? = null,
    val brandId: UUID? = null,
    val price: BigDecimal? = null,
    val status: ProductStatus? = null,
    val isActive: Boolean? = null,
    val imageUrl: String? = null,
    val specifications: Map<String, Any>? = null
)

