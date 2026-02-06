package com.bonusmart.product_api.persistence.repository

import com.bonusmart.product_api.persistence.entity.Category
import com.bonusmart.product_api.persistence.entity.CategoryClosure
import com.bonusmart.product_api.persistence.entity.CategoryClosureId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface CategoryClosureRepository : JpaRepository<CategoryClosure, CategoryClosureId> {


    @Query("""
        select cc.id.ancestorId
        from CategoryClosure cc
        where cc.id.descendantId = :categoryId
        order by cc.depth asc
    """)
    fun findAncestorIds(categoryId: UUID): List<UUID>

    @Query("""
        select cc.id.descendantId
        from CategoryClosure cc
        where cc.id.ancestorId = :categoryId
        order by cc.depth asc
    """)
    fun findDescendantIds(categoryId: UUID): List<UUID>

    fun findByIdDescendantId(descendantId: UUID): List<CategoryClosure>

    @Query("""
        select count(cc)
        from CategoryClosure cc
        where cc.id.ancestorId = :categoryId
          and cc.depth = 1
    """)
    fun countChildren(
       categoryId: UUID
    ): Long

    @Query("""
        select c
        from Category c
        join CategoryClosure cc
          on cc.id.descendantId = c.id
        where cc.id.ancestorId = :categoryId
          and cc.depth = 1
          and c.deleted = false
    """)
    fun findChildren(categoryId: UUID): List<Category>


}