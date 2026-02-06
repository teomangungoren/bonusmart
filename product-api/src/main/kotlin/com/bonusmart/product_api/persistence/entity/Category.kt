package com.bonusmart.product_api.persistence.entity

import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Where
import java.util.*

@Entity
@Table(name = "categories")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @Column(nullable = false, length = 255)
    var name: String,
    
    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(nullable = false)
    var deleted: Boolean = false
    
) : BaseAuditorAware() {

}