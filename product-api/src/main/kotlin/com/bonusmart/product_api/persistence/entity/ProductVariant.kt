package com.bonusmart.product_api.persistence.entity

import com.bonusmart.product_api.domain.enums.VariantType
import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(
    name = "product_variants",
    indexes = [
        Index(name = "idx_variant_product_id", columnList = "product_id"),
        Index(name = "idx_variant_deleted", columnList = "deleted")
    ]
)
data class ProductVariant(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    val variantType: VariantType,
    
    @Column(nullable = false, length = 100)
    val variantValue: String,
    
    @Column(nullable = false, precision = 19, scale = 2)
    val priceAdjustment: BigDecimal = BigDecimal.ZERO,
    
    @Column(nullable = false)
    val isActive: Boolean = false,
    
    @Column(nullable = false)
    val deleted: Boolean = false
) : BaseAuditorAware()
