package com.bonusmart.product_api.persistence.repository

import com.bonusmart.product_api.persistence.entity.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BrandRepository : JpaRepository<Brand, UUID> {
    
    fun findByNameAndDeletedFalse(name: String): Brand?
    fun findAllByDeletedFalse(): List<Brand>
    fun findAllByIsActiveTrueAndDeletedFalse(): List<Brand>
    fun existsByIdAndDeletedFalse(id: UUID): Boolean
    
    @Query("""
        SELECT b.name FROM Brand b
        WHERE b.id = :brandId
        AND b.deleted = false
    """)
    fun findBrandNameById(@Param("brandId") brandId: UUID): String?
}