package com.bonusmart.product_api.service

import com.bonusmart.product_api.persistence.entity.Category
import com.bonusmart.product_api.persistence.entity.CategoryClosure
import com.bonusmart.product_api.persistence.entity.CategoryClosureId
import com.bonusmart.product_api.persistence.repository.CategoryClosureRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CategoryClosureService(private val categoryClosureRepository: CategoryClosureRepository) {

    fun createCategoryClosure(category: Category, parentId: UUID?) {
        val categoryClosure = CategoryClosure(
            id = CategoryClosureId(category.id!!, category.id!!),
            ancestor = category,
            descendant = category,
            depth = 0
        )
        parentId?.let {
            val parentClosures = categoryClosureRepository.findByIdDescendantId(it)

            parentClosures.forEach { pc ->
                categoryClosureRepository.save(
                    CategoryClosure(
                        id = CategoryClosureId(
                            pc.id.ancestorId,
                            category.id!!
                        ),
                        ancestor = pc.ancestor,
                        descendant = category,
                        depth = pc.depth + 1
                    )
                )
            }
        }
    }

    fun getAncestorIds(categoryId: UUID): List<UUID> =
        categoryClosureRepository.findAncestorIds(categoryId)


    fun getDescendantIds(categoryId: UUID): List<UUID> =
        categoryClosureRepository.findDescendantIds(categoryId)


    fun countChildren(categoryId: UUID): Long =
        categoryClosureRepository.countChildren(categoryId)


    fun findChildren(categoryId: UUID): List<Category> =
        categoryClosureRepository.findChildren(categoryId)

    fun isRoot(categoryId: UUID): Boolean =
        categoryClosureRepository.countChildren(categoryId) == 0L &&
                categoryClosureRepository.findAncestorIds(categoryId).size == 1

    fun isLeaf(categoryId: UUID): Boolean =
        categoryClosureRepository.countChildren(categoryId) == 0L


}