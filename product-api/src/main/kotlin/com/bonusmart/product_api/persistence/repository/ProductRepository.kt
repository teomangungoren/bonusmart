package com.bonusmart.product_api.persistence.repository

import com.bonusmart.product_api.domain.enums.ProductStatus
import com.bonusmart.product_api.persistence.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, UUID> {
    
    fun findAllByDeletedFalse(): List<Product>
    fun findByStatusAndDeletedFalse(status: ProductStatus): List<Product>
    fun findByBrandIdAndDeletedFalse(brandId: UUID): List<Product>
    
    @Query("""
        SELECT DISTINCT p FROM Product p
        INNER JOIN ProductCategory pc ON p.id = pc.product.id
        WHERE pc.categoryId IN :categoryIds
        AND p.deleted = false
        AND p.isActive = true
    """)
    fun findByCategoryIds(@Param("categoryIds") categoryIds: List<UUID>): List<Product>
    
    @Query("""
        SELECT COUNT(DISTINCT p.id) FROM Product p
        INNER JOIN ProductCategory pc ON p.id = pc.product.id
        WHERE pc.categoryId IN :categoryIds
        AND p.deleted = false
        AND p.isActive = true
    """)
    fun countByCategoryIds(@Param("categoryIds") categoryIds: List<UUID>): Long
    
    @Query("""
        SELECT b.id FROM Product p
        INNER JOIN p.brand b
        WHERE p.id = :productId
        AND p.deleted = false
    """)
    fun findBrandIdByProductId(@Param("productId") productId: UUID): UUID?
}