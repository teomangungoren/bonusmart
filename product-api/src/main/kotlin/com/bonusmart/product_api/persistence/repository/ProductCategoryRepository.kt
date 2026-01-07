package com.bonusmart.product_api.persistence.repository

import com.bonusmart.product_api.persistence.entity.ProductCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductCategoryRepository : JpaRepository<ProductCategory, UUID> {
    
    fun findByProductId(productId: UUID): List<ProductCategory>
    fun existsByProductIdAndCategoryId(productId: UUID, categoryId: UUID): Boolean
    
    @Modifying
    @Query("DELETE FROM ProductCategory pc WHERE pc.product.id = :productId AND pc.categoryId = :categoryId")
    fun deleteByProductIdAndCategoryId(
        @Param("productId") productId: UUID,
        @Param("categoryId") categoryId: UUID
    ): Int
    
    @Modifying
    @Query("DELETE FROM ProductCategory pc WHERE pc.product.id = :productId")
    fun deleteByProductId(@Param("productId") productId: UUID)
    
    @Modifying
    @Query("DELETE FROM ProductCategory pc WHERE pc.categoryId = :categoryId")
    fun deleteByCategoryId(@Param("categoryId") categoryId: UUID)
}



