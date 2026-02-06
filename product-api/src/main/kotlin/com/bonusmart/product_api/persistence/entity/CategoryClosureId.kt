package com.bonusmart.product_api.persistence.entity

import jakarta.persistence.Embeddable
import java.util.UUID

@Embeddable
data class CategoryClosureId(
   val ancestorId: UUID,
   val descendantId: UUID
)
