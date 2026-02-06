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
    private val categoryService: CategoryService,
    private val categoryClosureService: CategoryClosureService
) {
    
    @Transactional
    fun createCategory(request: CreateCategoryRequest): CategoryResponse {
        val category = categoryService.create(
            name = request.name,
            description = request.description
        )

        categoryClosureService.createCategoryClosure(
            category = category,
            parentId = request.parentId
        )

        val childrenCount = categoryClosureService.countChildren(category.id!!)

        return CategoryResponse.from(category, childrenCount)
    }
    
    @Transactional
    fun updateCategory(categoryId: UUID, request: UpdateCategoryRequest): CategoryResponse {
        val category = categoryService.update(
            categoryId = categoryId,
            name = request.name,
            description = request.description
        )

        val childrenCount = categoryClosureService.countChildren(categoryId)

        return CategoryResponse.from(category, childrenCount)
    }
    
    @Transactional
    fun deleteCategory(categoryId: UUID) {
        categoryService.softDelete(categoryId)
    }

    fun retrieveCategoryById(categoryId: UUID): CategoryResponse {
        val category = categoryService.findById(categoryId)
        val childrenCount = categoryClosureService.countChildren(categoryId)

        return CategoryResponse.from(category, childrenCount)
    }
}


