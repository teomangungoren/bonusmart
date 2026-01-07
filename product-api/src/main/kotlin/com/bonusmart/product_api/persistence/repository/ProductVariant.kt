package com.bonusmart.product_api.persistence.repository

import com.bonusmart.product_api.persistence.entity.ProductVariant
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductVariant : JpaRepository<ProductVariant, UUID> {
}