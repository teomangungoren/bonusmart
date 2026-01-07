package com.bonusmart.product_api.domain.response

import com.bonusmart.product_api.persistence.entity.Brand
import java.time.Instant
import java.util.UUID

data class BrandResponse(
    val id: UUID,
    val name: String,
    val description: String?,
    val slug: String,
    val logoUrl: String?,
    val isActive: Boolean,
    val createdAt: Instant?,
    val updatedAt: Instant?
) {
    companion object {
        fun from(brand: Brand): BrandResponse {
            return BrandResponse(
                id = brand.id!!,
                name = brand.name,
                description = brand.description,
                slug = brand.slug,
                logoUrl = brand.logoUrl,
                isActive = brand.isActive,
                createdAt = brand.createdDate,
                updatedAt = brand.updatedDate
            )
        }
    }
}



