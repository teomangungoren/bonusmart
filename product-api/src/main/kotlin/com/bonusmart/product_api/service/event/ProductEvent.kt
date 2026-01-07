package com.bonusmart.product_api.service.event

import com.fasterxml.jackson.databind.JsonNode
import java.time.Instant
import java.util.UUID

data class ProductEvent(
    val id: UUID,
    val name: String?,
    val description: String?,
    val brandId: UUID?,
    val price: String?,
    val status: String?,
    val isActive: Boolean?,
    val imageUrl: String?,
    val specifications: JsonNode?,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val deleted: Boolean,
    val operation: ProductEventOperation,
    val isDeleted: Boolean
)

