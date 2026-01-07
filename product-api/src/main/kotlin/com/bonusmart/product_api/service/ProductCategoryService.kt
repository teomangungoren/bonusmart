package com.bonusmart.product_api.service

import com.bonusmart.product_api.persistence.document.ProductDocument
import com.bonusmart.product_api.persistence.entity.Product
import com.bonusmart.product_api.persistence.entity.ProductCategory
import com.bonusmart.product_api.persistence.repository.ProductCategoryRepository
import com.bonusmart.product_api.persistence.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProductCategoryService(
    private val productRepository: ProductRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val categoryService: CategoryService,
    private val productDocumentService: ProductDocumentService,
    private val productService: ProductService
) {
    private val logger = LoggerFactory.getLogger(ProductCategoryService::class.java)
    
    @Transactional
    fun assignProductToCategory(productId: UUID, categoryId: UUID) {
        logger.info("Assigning product $productId to category $categoryId")
        
        validateProductExists(productId)
        categoryService.validateCategoryExists(categoryId)
        
        val product = productRepository.findById(productId)
            .orElseThrow { NoSuchElementException("Product not found: $productId") }
        
        logger.debug("Fetching ancestor categories for category $categoryId")
        val ancestorCategoryIds = categoryService.getAncestorIds(categoryId)
        logger.debug("Found ${ancestorCategoryIds.size} ancestor categories")
        
        val newCategories = ancestorCategoryIds
            .filterNot { 
                productCategoryRepository.existsByProductIdAndCategoryId(productId, it) 
            }
            .map { 
                ProductCategory(product = product, categoryId = it) 
            }
        
        if (newCategories.isNotEmpty()) {
            productCategoryRepository.saveAll(newCategories)
            logger.info("Successfully assigned product $productId to ${newCategories.size} categories")
            
            syncProductDocumentToElasticsearch(productId)
        } else {
            logger.debug("Product $productId already assigned to all ancestor categories")
        }
    }
    
    @Transactional
    fun removeProductFromCategory(productId: UUID, categoryId: UUID) {
        logger.info("Removing product $productId from category $categoryId")
        
        validateProductExists(productId)
        
        val deletedCount = productCategoryRepository.deleteByProductIdAndCategoryId(productId, categoryId)
        
        if (deletedCount > 0) {
            logger.info("Successfully removed product $productId from category $categoryId")
            
            syncProductDocumentToElasticsearch(productId)
        } else {
            logger.warn("Product $productId was not assigned to category $categoryId")
        }
    }
    
    @Transactional(readOnly = true)
    fun getCategoryIdsForProduct(productId: UUID): List<UUID> {
        return productCategoryRepository.findByProductId(productId)
            .map { it.categoryId }
    }
    
    @Transactional(readOnly = true)
    fun getProductsByCategory(categoryId: UUID): List<Product> {
        val descendantCategoryIds = categoryService.getDescendantIds(categoryId)
        return productRepository.findByCategoryIds(descendantCategoryIds)
    }
    
    @Transactional(readOnly = true)
    fun getProductCountByCategory(categoryId: UUID): Long {
        val descendantCategoryIds = categoryService.getDescendantIds(categoryId)
        return productRepository.countByCategoryIds(descendantCategoryIds)
    }
    
    private fun validateProductExists(productId: UUID) {
        if (!productRepository.existsById(productId)) {
            throw NoSuchElementException("Product not found: $productId")
        }
    }
    
    private fun syncProductDocumentToElasticsearch(productId: UUID) {
        try {
            logger.debug("Syncing product $productId to Elasticsearch")
            
            val product = productService.retrieveProductById(productId)
            val categoryIds = getCategoryIdsForProduct(productId)
            
            val existingDocument = productDocumentService.findById(productId)
            
            val productDocument = existingDocument?.copy(
                categoryIds = categoryIds
            ) ?: run {
                ProductDocument(
                    id = productId,
                    name = product.name,
                    description = product.description,
                    brandId = product.brand.id ?: throw IllegalStateException("Brand ID is null"),
                    brandName = product.brand.name,
                    price = product.price,
                    status = product.status.toString(),
                    isActive = product.isActive,
                    imageUrl = product.imageUrl,
                    specifications = product.specifications,
                    categoryIds = categoryIds,
                    createdAt = product.createdDate,
                    updatedAt = product.updatedDate,
                    deleted = product.deleted
                )
            }
            
            productDocumentService.updateProduct(productDocument)
            logger.debug("Successfully synced product $productId to Elasticsearch")
        } catch (e: Exception) {
            logger.error("Failed to sync product $productId to Elasticsearch", e)
        }
    }
}
