package com.bonusmart.product_api.controller

import com.bonusmart.product_api.domain.request.CreateProductRequest
import com.bonusmart.product_api.domain.request.UpdateProductRequest
import com.bonusmart.product_api.domain.response.ProductResponse
import com.bonusmart.product_api.domain.response.ProductSearchResponse
import com.bonusmart.product_api.service.ProductApplicationService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*

@RestController
@RequestMapping("/products")
class ProductController(
    private val productApplicationService: ProductApplicationService
) {
    
    @PostMapping
    fun createProduct(
        @RequestBody request: CreateProductRequest
    ): ResponseEntity<ProductResponse> {
        val product = productApplicationService.createProduct(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(product)
    }
    
    @GetMapping("/{productId}")
    fun getProductById(
        @PathVariable productId: UUID
    ): ResponseEntity<ProductResponse> {
        val product = productApplicationService.retrieveProductById(productId)
        return ResponseEntity.ok(product)
    }
    
    @GetMapping
    fun getAllProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ProductResponse>> {
        val products = productApplicationService.retrieveAllProducts(page, size)
        return ResponseEntity.ok(products)
    }
    
    @GetMapping("/search")
    fun searchProducts(
        @RequestParam(required = false) q: String?,
        @RequestParam(required = false) categoryIds: List<UUID>?,
        @RequestParam(required = false) brandIds: List<UUID>?,
        @RequestParam(required = false) status: String?,
        @RequestParam(required = false) minPrice: BigDecimal?,
        @RequestParam(required = false) maxPrice: BigDecimal?,
        @RequestParam(required = false) specifications: Map<String, Any>?,
        @RequestParam(defaultValue = "relevance") sortBy: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ProductSearchResponse> {
        val result = productApplicationService.searchProducts(
            q, categoryIds, brandIds, status, minPrice, maxPrice, specifications, sortBy, page, size
        )
        return ResponseEntity.ok(result)
    }
    
    @GetMapping("/search/suggestions")
    fun getSearchSuggestions(
        @RequestParam q: String,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<List<String>> {
        val suggestions = productApplicationService.getSearchSuggestions(q, limit)
        return ResponseEntity.ok(suggestions)
    }
    
    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable productId: UUID,
        @RequestBody request: UpdateProductRequest
    ): ResponseEntity<ProductResponse> {
        val product = productApplicationService.updateProduct(productId, request)
        return ResponseEntity.ok(product)
    }
    
    @DeleteMapping("/{productId}")
    fun deleteProduct(
        @PathVariable productId: UUID
    ): ResponseEntity<Void> {
        productApplicationService.deleteProduct(productId)
        return ResponseEntity.noContent().build()
    }
    
    @PostMapping("/{productId}/categories/{categoryId}")
    fun assignCategory(
        @PathVariable productId: UUID,
        @PathVariable categoryId: UUID
    ): ResponseEntity<Void> {
        productApplicationService.assignCategoryToProduct(productId, categoryId)
        return ResponseEntity.noContent().build()
    }
    
    @DeleteMapping("/{productId}/categories/{categoryId}")
    fun removeCategory(
        @PathVariable productId: UUID,
        @PathVariable categoryId: UUID
    ): ResponseEntity<Void> {
        productApplicationService.removeCategoryFromProduct(productId, categoryId)
        return ResponseEntity.noContent().build()
    }
    
    @GetMapping("/categories/{categoryId}")
    fun getProductsByCategory(
        @PathVariable categoryId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ProductResponse>> {
        val products = productApplicationService.getProductsByCategory(categoryId, page, size)
        return ResponseEntity.ok(products)
    }
}

