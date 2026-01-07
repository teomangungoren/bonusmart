package com.bonusmart.product_api.persistence.repository

import com.bonusmart.product_api.persistence.entity.ProductVariant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductVariantRepository : JpaRepository<ProductVariant, UUID> {
    
    fun findByProductIdAndDeletedFalse(productId: UUID): List<ProductVariant>
}

