package com.bonusmart.product_api.persistence.entity

import com.bonusmart.product_api.domain.enums.ProductStatus
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.math.BigDecimal
import java.util.*

@Entity
@Table(
    name = "products",
    indexes = [
        Index(name = "idx_product_brand_id", columnList = "brand_id"),
        Index(name = "idx_product_status", columnList = "status"),
        Index(name = "idx_product_deleted", columnList = "deleted")
    ]
)
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @Column(nullable = false, length = 255)
    var name: String,
    
    @Column(columnDefinition = "TEXT")
    var description: String? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    var brand: Brand,
    
    @Column(nullable = false, precision = 19, scale = 2)
    var price: BigDecimal,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    var status: ProductStatus = ProductStatus.DRAFT,
    
    @Column(nullable = false)
    var isActive: Boolean = true,
    
    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    val variants: MutableList<ProductVariant> = mutableListOf(),
    
    @Column(length = 500)
    var imageUrl: String? = null,
    
    @Column(columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    var specifications: Map<String, Any>? = null,
    
    @Column(nullable = false)
    var deleted: Boolean = false
) : BaseAuditorAware()
