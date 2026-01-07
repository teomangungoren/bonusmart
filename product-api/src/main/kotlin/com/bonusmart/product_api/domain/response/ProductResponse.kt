package com.bonusmart.product_api.domain.response

import com.bonusmart.product_api.domain.enums.ProductStatus
import com.bonusmart.product_api.persistence.document.ProductDocument
import com.bonusmart.product_api.persistence.entity.Product
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class ProductResponse(
    val id: UUID,
    val name: String,
    val description: String?,
    val brandId: UUID,
    val brandName: String,
    val price: BigDecimal,
    val status: ProductStatus,
    val isActive: Boolean,
    val imageUrl: String?,
    val specifications: Map<String, Any>?,
    val categoryIds: List<UUID>,
    val createdAt: Instant?,
    val updatedAt: Instant?
) {
    companion object {
        fun from(product: Product, categoryIds: List<UUID>): ProductResponse {
            return ProductResponse(
                id = product.id!!,
                name = product.name,
                description = product.description,
                brandId = product.brand.id!!,
                brandName = product.brand.name,
                price = product.price,
                status = product.status,
                isActive = product.isActive,
                imageUrl = product.imageUrl,
                specifications = product.specifications,
                categoryIds = categoryIds,
                createdAt = product.createdDate,
                updatedAt = product.updatedDate
            )
        }
        
        fun fromDocument(document: ProductDocument): ProductResponse {
            return ProductResponse(
                id = document.id,
                name = document.name,
                description = document.description,
                brandId = document.brandId,
                brandName = document.brandName,
                price = document.price,
                status = ProductStatus.valueOf(document.status),
                isActive = document.isActive,
                imageUrl = document.imageUrl,
                specifications = document.specifications,
                categoryIds = document.categoryIds,
                createdAt = document.createdAt,
                updatedAt = document.updatedAt
            )
        }
    }
}

