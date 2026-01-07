package com.bonusmart.product_api.service

import com.bonusmart.product_api.domain.request.CreateCategoryRequest
import com.bonusmart.product_api.domain.request.UpdateCategoryRequest
import com.bonusmart.product_api.domain.response.CategoryResponse
import com.bonusmart.product_api.domain.response.CategoryTreeResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CategoryApplicationService(
    private val categoryService: CategoryService
) {
    
    @Transactional
    fun createCategory(request: CreateCategoryRequest): CategoryResponse {
        val category = categoryService.createCategory(
            name = request.name,
            description = request.description,
            parentId = request.parentId
        )
        
        val childrenCount = category.children.size
        return CategoryResponse.from(category, childrenCount)
    }
    
    @Transactional
    fun updateCategory(categoryId: UUID, request: UpdateCategoryRequest): CategoryResponse {
        val category = categoryService.updateCategory(
            categoryId = categoryId,
            name = request.name,
            description = request.description
        )
        
        val childrenCount = category.children.size
        return CategoryResponse.from(category, childrenCount)
    }
    
    @Transactional
    fun deleteCategory(categoryId: UUID) {
        categoryService.deleteCategory(categoryId)
    }
    
    @Transactional(readOnly = true)
    fun retrieveCategoryById(categoryId: UUID): CategoryResponse {
        val category = categoryService.findByIdWithChildren(categoryId)
        val childrenCount = category.children.size
        return CategoryResponse.from(category, childrenCount)
    }
    
    @Transactional(readOnly = true)
    fun retrieveAllRootCategories(): List<CategoryResponse> {
        val categories = categoryService.findAllRootCategoriesWithChildren()
        return categories.map { CategoryResponse.from(it, it.children.size) }
    }
    
    @Transactional(readOnly = true)
    fun retrieveCategoryTree(rootCategoryId: UUID? = null): List<CategoryTreeResponse> {
        val rootCategories = if (rootCategoryId != null) {
            listOf(categoryService.findByIdWithChildren(rootCategoryId))
        } else {
            categoryService.findAllRootCategoriesWithChildren()
        }
        
        return rootCategories
            .filter { !it.deleted }
            .map { CategoryTreeResponse.from(it) }
    }
    
    @Transactional(readOnly = true)
    fun retrieveAncestorCategoryIdsIncludingSelf(categoryId: UUID): List<UUID> {
        return categoryService.getAncestorIds(categoryId)
    }
    
    @Transactional(readOnly = true)
    fun retrieveDescendantCategoryIdsIncludingSelf(categoryId: UUID): List<UUID> {
        return categoryService.getDescendantIds(categoryId)
    }
}

