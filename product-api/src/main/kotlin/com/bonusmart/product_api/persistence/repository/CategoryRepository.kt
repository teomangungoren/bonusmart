package com.bonusmart.product_api.persistence.repository

import com.bonusmart.product_api.persistence.entity.Category
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CategoryRepository : JpaRepository<Category, UUID> {

    @EntityGraph(attributePaths = ["children"])
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.deleted = false ORDER BY c.name")
    fun findAllRootCategoriesWithChildren(): List<Category>
    

    @EntityGraph(attributePaths = ["children"])
    @Query("SELECT c FROM Category c WHERE c.id = :id AND c.deleted = false")
    fun findByIdWithChildren(@Param("id") id: UUID): Optional<Category>
    

    @Query(
        value = """
            WITH RECURSIVE ancestors AS (
                SELECT id, parent_id FROM categories WHERE id = :categoryId AND deleted = false
                UNION ALL
                SELECT c.id, c.parent_id FROM categories c
                INNER JOIN ancestors a ON c.id = a.parent_id
                WHERE c.deleted = false
            )
            SELECT id FROM ancestors
        """,
        nativeQuery = true
    )
    fun findAncestorIds(@Param("categoryId") categoryId: UUID): List<UUID>
    

    @Query(
        value = """
            WITH RECURSIVE descendants AS (
                SELECT id, parent_id FROM categories WHERE id = :categoryId AND deleted = false
                UNION ALL
                SELECT c.id, c.parent_id FROM categories c
                INNER JOIN descendants d ON c.parent_id = d.id
                WHERE c.deleted = false
            )
            SELECT id FROM descendants
        """,
        nativeQuery = true
    )
    fun findDescendantIds(@Param("categoryId") categoryId: UUID): List<UUID>
    

    @Query("""
        SELECT c FROM Category c 
        WHERE c.parent.id = :parentId 
        AND c.deleted = false 
        ORDER BY c.name ASC
    """)
    fun findChildrenByParentId(@Param("parentId") parentId: UUID): List<Category>
    

    @Query("""
        SELECT c FROM Category c 
        WHERE c.deleted = false 
        ORDER BY c.name ASC
    """)
    fun findAllActiveCategories(): List<Category>
    

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.id = :id AND c.deleted = false")
    fun existsByIdAndDeletedFalse(@Param("id") id: UUID): Boolean
}

