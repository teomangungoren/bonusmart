package com.bonusmart.product_api.domain.response

import com.bonusmart.product_api.persistence.entity.Category
import java.util.UUID

data class CategoryTreeResponse(
    val id: UUID,
    val name: String,
    val description: String?,
    val children: List<CategoryTreeResponse>
) {
    companion object {
        fun from(category: Category): CategoryTreeResponse {
            return CategoryTreeResponse(
                id = category.id!!,
                name = category.name,
                description = category.description,
                children = category.children
                    .filter { !it.deleted }
                    .map { from(it) }
            )
        }
    }
}

