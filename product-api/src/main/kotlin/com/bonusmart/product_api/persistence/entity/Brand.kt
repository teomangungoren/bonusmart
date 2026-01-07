package com.bonusmart.product_api.persistence.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "brands",
    indexes = [
        Index(name = "idx_brand_name", columnList = "name"),
        Index(name = "idx_brand_deleted", columnList = "deleted")
    ]
)
data class Brand(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @Column(nullable = false, length = 255)
    var name: String,
    
    @Column(columnDefinition = "TEXT")
    var description: String? = null,
    
    @Column(nullable = false, length = 255)
    var slug: String,
    
    @Column(length = 500)
    var logoUrl: String? = null,
    
    @Column(nullable = false)
    var isActive: Boolean = true,
    
    @Column(nullable = false)
    var deleted: Boolean = false
) : BaseAuditorAware()
