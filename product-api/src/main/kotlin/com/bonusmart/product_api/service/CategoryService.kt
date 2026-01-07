package com.bonusmart.product_api.service

import com.bonusmart.product_api.persistence.entity.Category
import com.bonusmart.product_api.persistence.repository.CategoryRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    private val logger = LoggerFactory.getLogger(CategoryService::class.java)
    
    @Transactional(readOnly = true)
    fun findById(categoryId: UUID): Category {
        return categoryRepository.findById(categoryId)
            .filter { !it.deleted }
            .orElseThrow { NoSuchElementException("Category not found: $categoryId") }
    }
    
    @Transactional(readOnly = true)
    fun findAllRootCategoriesWithChildren(): List<Category> {
        logger.debug("Retrieving all root categories with children")
        return categoryRepository.findAllRootCategoriesWithChildren()
    }
    
    @Transactional(readOnly = true)
    fun findByIdWithChildren(categoryId: UUID): Category {
        logger.debug("Retrieving category $categoryId with children")
        return categoryRepository.findByIdWithChildren(categoryId)
            .orElseThrow { NoSuchElementException("Category not found: $categoryId") }
    }
    
    @Transactional(readOnly = true)
    fun getAncestorIds(categoryId: UUID): List<UUID> {
        logger.debug("Retrieving ancestor IDs for category $categoryId")
        return categoryRepository.findAncestorIds(categoryId)
    }
    
    @Transactional(readOnly = true)
    fun getDescendantIds(categoryId: UUID): List<UUID> {
        logger.debug("Retrieving descendant IDs for category $categoryId")
        return categoryRepository.findDescendantIds(categoryId)
    }
    
    @Transactional(readOnly = true)
    fun findAllActiveCategories(): List<Category> {
        return categoryRepository.findAllActiveCategories()
    }
    
    @Transactional(readOnly = true)
    fun findChildrenByParentId(parentId: UUID): List<Category> {
        return categoryRepository.findChildrenByParentId(parentId)
    }
    
    fun validateCategoryExists(categoryId: UUID) {
        if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
            throw NoSuchElementException("Category not found: $categoryId")
        }
    }
    
    @Transactional
    fun createCategory(name: String, description: String? = null, parentId: UUID? = null): Category {
        logger.info("Creating category: $name${parentId?.let { " (parent: $it)" } ?: ""}")
        
        val parent = parentId?.let { findById(it) }
        val category = Category(
            name = name,
            description = description,
            parent = parent
        )
        
        val saved = categoryRepository.save(category)
        logger.info("Successfully created category: ${saved.id}")
        return saved
    }
    
    @Transactional
    fun updateCategory(categoryId: UUID, name: String? = null, description: String? = null): Category {
        logger.info("Updating category: $categoryId")
        
        val category = findById(categoryId)
        name?.let { category.name = it }
        description?.let { category.description = it }
        
        val updated = categoryRepository.save(category)
        logger.info("Successfully updated category: $categoryId")
        return updated
    }
    
    @Transactional
    fun deleteCategory(categoryId: UUID) {
        logger.info("Deleting category: $categoryId")
        
        val category = findById(categoryId)
        category.deleted = true
        categoryRepository.save(category)
        
        logger.info("Successfully deleted category: $categoryId")
    }
}

