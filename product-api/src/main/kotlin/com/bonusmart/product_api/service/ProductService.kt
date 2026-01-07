package com.bonusmart.product_api.service

import com.bonusmart.product_api.persistence.entity.Product
import com.bonusmart.product_api.persistence.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val brandService: BrandService
) {
    
    @Transactional(readOnly = true)
    fun retrieveProductById(productId: UUID): Product {
        return productRepository.findById(productId)
            .filter { !it.deleted }
            .orElseThrow { NoSuchElementException("Product not found: $productId") }
    }
    
    @Transactional
    fun createProduct(product: Product): Product {
        product.brand.id?.let { brandId ->
            brandService.retrieveBrandById(brandId)
        } ?: throw IllegalArgumentException("Brand ID is required")
        
        return productRepository.save(product)
    }
    
    @Transactional
    fun updateProduct(product: Product): Product {
        retrieveProductById(product.id ?: throw IllegalArgumentException("Product ID is required"))
        return productRepository.save(product)
    }
    
    @Transactional
    fun deleteProduct(productId: UUID) {
        retrieveProductById(productId)
            .apply { deleted = true }
            .let { productRepository.save(it) }
    }
    
    @Transactional(readOnly = true)
    fun retrieveAllProducts(): List<Product> {
        return productRepository.findAllByDeletedFalse()
    }
    
    @Transactional(readOnly = true)
    fun getBrandIdByProductId(productId: UUID): UUID {
        return productRepository.findBrandIdByProductId(productId)
            ?: throw NoSuchElementException("Product not found or has no brand: $productId")
    }
}

