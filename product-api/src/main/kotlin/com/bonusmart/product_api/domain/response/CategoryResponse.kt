package com.bonusmart.product_api.domain.response

import com.bonusmart.product_api.persistence.entity.Category
import java.time.Instant
import java.util.UUID

data class CategoryResponse(
    val id: UUID,
    val name: String,
    val description: String?,
    val childrenCount: Long,
    val createdAt: Instant?,
    val updatedAt: Instant?
) {
    companion object {
        fun from(category: Category, childrenCount: Long = 0): CategoryResponse {
            return CategoryResponse(
                id = category.id!!,
                name = category.name,
                description = category.description,
                childrenCount = childrenCount,
                createdAt = category.createdDate,
                updatedAt = category.updatedDate
            )
        }
    }
}


