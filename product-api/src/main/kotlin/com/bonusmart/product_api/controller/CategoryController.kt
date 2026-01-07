package com.bonusmart.product_api.controller

import com.bonusmart.product_api.domain.request.CreateCategoryRequest
import com.bonusmart.product_api.domain.request.UpdateCategoryRequest
import com.bonusmart.product_api.domain.response.CategoryResponse
import com.bonusmart.product_api.domain.response.CategoryTreeResponse
import com.bonusmart.product_api.service.CategoryApplicationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(
    private val categoryApplicationService: CategoryApplicationService
) {
    
    @PostMapping
    fun createCategory(
        @RequestBody request: CreateCategoryRequest
    ): ResponseEntity<CategoryResponse> {
        val category = categoryApplicationService.createCategory(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(category)
    }
    
    @GetMapping("/{categoryId}")
    fun getCategoryById(
        @PathVariable categoryId: UUID
    ): ResponseEntity<CategoryResponse> {
        val category = categoryApplicationService.retrieveCategoryById(categoryId)
        return ResponseEntity.ok(category)
    }
    
    @GetMapping
    fun getAllRootCategories(): ResponseEntity<List<CategoryResponse>> {
        val categories = categoryApplicationService.retrieveAllRootCategories()
        return ResponseEntity.ok(categories)
    }
    
    @GetMapping("/tree")
    fun getCategoryTree(
        @RequestParam(required = false) rootCategoryId: UUID?
    ): ResponseEntity<List<CategoryTreeResponse>> {
        val tree = categoryApplicationService.retrieveCategoryTree(rootCategoryId)
        return ResponseEntity.ok(tree)
    }
    
    @PutMapping("/{categoryId}")
    fun updateCategory(
        @PathVariable categoryId: UUID,
        @RequestBody request: UpdateCategoryRequest
    ): ResponseEntity<CategoryResponse> {
        val category = categoryApplicationService.updateCategory(categoryId, request)
        return ResponseEntity.ok(category)
    }
    
    @DeleteMapping("/{categoryId}")
    fun deleteCategory(
        @PathVariable categoryId: UUID
    ): ResponseEntity<Void> {
        categoryApplicationService.deleteCategory(categoryId)
        return ResponseEntity.noContent().build()
    }
    
    @GetMapping("/{categoryId}/ancestors")
    fun getAncestorCategoryIds(
        @PathVariable categoryId: UUID
    ): ResponseEntity<List<UUID>> {
        val ancestorIds = categoryApplicationService.retrieveAncestorCategoryIdsIncludingSelf(categoryId)
        return ResponseEntity.ok(ancestorIds)
    }
    
    @GetMapping("/{categoryId}/descendants")
    fun getDescendantCategoryIds(
        @PathVariable categoryId: UUID
    ): ResponseEntity<List<UUID>> {
        val descendantIds = categoryApplicationService.retrieveDescendantCategoryIdsIncludingSelf(categoryId)
        return ResponseEntity.ok(descendantIds)
    }
}

