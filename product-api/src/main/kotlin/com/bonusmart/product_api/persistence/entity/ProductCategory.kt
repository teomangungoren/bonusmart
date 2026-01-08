package com.bonusmart.product_api.persistence.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(
    name = "product_categories",
    indexes = [
        Index(name = "idx_product_category_product_id", columnList = "product_id"),
        Index(name = "idx_product_category_category_id", columnList = "category_id")
    ],
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_product_category",
            columnNames = ["product_id", "category_id"]
        )
    ]
)
data class ProductCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,
    
    @Column(nullable = false)
    val categoryId: UUID,
    
    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
)




