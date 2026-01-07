package com.bonusmart.product_api.service

import com.bonusmart.product_api.domain.enums.ProductStatus
import com.bonusmart.product_api.domain.request.CreateProductRequest
import com.bonusmart.product_api.domain.request.UpdateProductRequest
import com.bonusmart.product_api.domain.response.ProductResponse
import com.bonusmart.product_api.domain.response.ProductSearchResponse
import com.bonusmart.product_api.persistence.entity.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class ProductApplicationService(
    private val productService: ProductService,
    private val productCategoryService: ProductCategoryService,
    private val brandService: BrandService,
    private val productSearchService: ProductSearchService
) {
    
    @Transactional
    fun createProduct(request: CreateProductRequest): ProductResponse {
        val brand = brandService.retrieveBrandById(request.brandId)
        
        val product = Product(
            name = request.name,
            description = request.description,
            brand = brand,
            price = request.price,
            status = request.status ?: ProductStatus.DRAFT,
            isActive = request.isActive ?: true,
            imageUrl = request.imageUrl,
            specifications = request.specifications
        )
        
        val saved = productService.createProduct(product)
        
        saved.id?.let { productId ->
            request.categoryId?.let { productCategoryService.assignProductToCategory(productId, it) }
            val categoryIds = productCategoryService.getCategoryIdsForProduct(productId)
            return ProductResponse.from(saved, categoryIds)
        } ?: throw IllegalStateException("Product ID is null after creation")
    }
    
    @Transactional
    fun updateProduct(productId: UUID, request: UpdateProductRequest): ProductResponse {
        val product = productService.retrieveProductById(productId)
        
        request.name?.let { product.name = it }
        request.description?.let { product.description = it }
        request.price?.let { product.price = it }
        request.status?.let { product.status = it }
        request.isActive?.let { product.isActive = it }
        request.imageUrl?.let { product.imageUrl = it }
        request.specifications?.let { product.specifications = it }
        
        request.brandId?.let { brandId ->
            val brand = brandService.retrieveBrandById(brandId)
            product.brand = brand
        }
        
        val updated = productService.updateProduct(product)
        val categoryIds = productCategoryService.getCategoryIdsForProduct(productId)
        return ProductResponse.from(updated, categoryIds)
    }
    
    @Transactional(readOnly = true)
    fun retrieveProductById(productId: UUID): ProductResponse {
        val productDocument = productSearchService.findById(productId)
            ?: throw NoSuchElementException("Product not found: $productId")
        return ProductResponse.fromDocument(productDocument)
    }
    
    @Transactional(readOnly = true)
    fun retrieveAllProducts(page: Int = 0, size: Int = 20): Page<ProductResponse> {
        val pageable = PageRequest.of(page, size)
        return productSearchService.findAll(pageable)
            .map { ProductResponse.fromDocument(it) }
    }
    
    @Transactional
    fun deleteProduct(productId: UUID) {
        productService.deleteProduct(productId)
    }
    
    @Transactional
    fun assignCategoryToProduct(productId: UUID, categoryId: UUID) {
        productCategoryService.assignProductToCategory(productId, categoryId)
    }
    
    @Transactional
    fun removeCategoryFromProduct(productId: UUID, categoryId: UUID) {
        productCategoryService.removeProductFromCategory(productId, categoryId)
    }
    
    @Transactional(readOnly = true)
    fun getProductsByCategory(categoryId: UUID, page: Int = 0, size: Int = 20): Page<ProductResponse> {
        val pageable = PageRequest.of(page, size)
        return productSearchService.findByCategory(categoryId, pageable)
            .map { ProductResponse.fromDocument(it) }
    }
    
    fun searchProducts(
        query: String?,
        categoryIds: List<UUID>?,
        brandIds: List<UUID>?,
        status: String?,
        minPrice: BigDecimal?,
        maxPrice: BigDecimal?,
        specifications: Map<String, Any>?,
        sortBy: String,
        page: Int,
        size: Int
    ): ProductSearchResponse {
        return productSearchService.searchProducts(
            query, categoryIds, brandIds, status, minPrice, maxPrice, specifications, sortBy, page, size
        )
    }
    
    fun getSearchSuggestions(query: String, limit: Int): List<String> {
        return productSearchService.getSuggestions(query, limit)
    }
}

