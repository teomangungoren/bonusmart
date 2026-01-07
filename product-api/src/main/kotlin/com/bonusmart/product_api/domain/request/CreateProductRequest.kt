package com.bonusmart.product_api.domain.request

import com.bonusmart.product_api.domain.enums.ProductStatus
import java.math.BigDecimal
import java.util.UUID

data class CreateProductRequest(
    val name: String,
    val description: String? = null,
    val brandId: UUID,
    val price: BigDecimal,
    val status: ProductStatus? = ProductStatus.DRAFT,
    val isActive: Boolean? = true,
    val imageUrl: String? = null,
    val specifications: Map<String, Any>? = null,
    val categoryId: UUID
)

