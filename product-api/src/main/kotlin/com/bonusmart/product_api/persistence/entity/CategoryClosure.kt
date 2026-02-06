package com.bonusmart.product_api.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId

@Entity
data class CategoryClosure(
    @EmbeddedId
    val id: CategoryClosureId,
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ancestorId")
    @JoinColumn(name = "ancestor_id")
    val ancestor: Category,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("descendantId")
    @JoinColumn(name = "descendant_id")
    val descendant: Category,

    @Column(nullable = false)
    val depth: Int
)
