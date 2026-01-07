package com.bonusmart.product_api.persistence.entity

import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.util.*

@Entity
@Table(
    name = "categories",
    indexes = [
        Index(name = "idx_category_parent_id", columnList = "parent_id"),
        Index(name = "idx_category_deleted", columnList = "deleted"),
        Index(name = "idx_category_active_deleted", columnList = "is_active, deleted")
    ]
)
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @Column(nullable = false, length = 255)
    var name: String,
    
    @Column(columnDefinition = "TEXT")
    var description: String? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Category? = null,
    
    @OneToMany(
        mappedBy = "parent",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @BatchSize(size = 20)
    val children: MutableList<Category> = mutableListOf(),

    
    @Column(nullable = false)
    var deleted: Boolean = false
    
) : BaseAuditorAware() {
    
    fun isRoot(): Boolean = parent == null
    
    fun isLeaf(): Boolean = children.isEmpty() || children.all { it.deleted }
    
    fun addChild(child: Category) {
        children.add(child)
        child.parent = this
    }
    
    fun removeChild(child: Category) {
        children.remove(child)
        child.parent = null
    }
}
