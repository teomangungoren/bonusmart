package com.bonusmart.product_api.service

import com.bonusmart.product_api.persistence.entity.Category
import com.bonusmart.product_api.persistence.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    @Transactional(readOnly = true)
    fun findById(categoryId: UUID): Category =
        categoryRepository.findById(categoryId)
            .filter { !it.deleted }
            .orElseThrow { NoSuchElementException("Category not found: $categoryId") }

    @Transactional(readOnly = true)
    fun findAll(): List<Category> =
        categoryRepository.findAll()
            .filter { !it.deleted }

    @Transactional
    fun create(
        name: String,
        description: String?
    ): Category {
        val category = Category(name = name, description = description)
       return  categoryRepository.save(category)
    }

    @Transactional
    fun update(
        categoryId: UUID,
        name: String?,
        description: String?
    ): Category {
        val category = findById(categoryId)
        name?.let { category.name = it }
        description?.let { category.description = it }
        return categoryRepository.save(category)
    }

    @Transactional
    fun softDelete(categoryId: UUID) {
        val category = findById(categoryId)
        category.deleted = true
        categoryRepository.save(category)
    }

    fun exists(categoryId: UUID): Boolean =
        categoryRepository.existsByIdAndDeletedFalse(categoryId)
}



