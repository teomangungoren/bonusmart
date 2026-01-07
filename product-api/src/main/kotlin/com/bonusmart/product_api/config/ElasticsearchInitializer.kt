package com.bonusmart.product_api.config

import com.bonusmart.product_api.persistence.document.ProductDocument
import com.bonusmart.product_api.persistence.entity.Product
import com.bonusmart.product_api.persistence.repository.ProductDocumentRepository
import com.bonusmart.product_api.service.ProductCategoryService
import com.bonusmart.product_api.service.ProductDocumentService
import com.bonusmart.product_api.service.ProductService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ElasticsearchInitializer(
    private val productService: ProductService,
    private val productCategoryService: ProductCategoryService,
    private val productDocumentService: ProductDocumentService,
    private val productDocumentRepository: ProductDocumentRepository
) : ApplicationRunner {

    @Value("\${elasticsearch.initial-sync.enabled:true}")
    private val initialSyncEnabled: Boolean = true

    override fun run(args: ApplicationArguments?) {
        if (!initialSyncEnabled) {
            return
        }

        try {
            val existingCount = productDocumentRepository.count()
            if (existingCount > 0) {
                return
            }

            val products = productService.retrieveAllProducts()
            val productDocuments = products.mapNotNull { product ->
                product.id?.let { productId ->
                    val categoryIds = productCategoryService.getCategoryIdsForProduct(productId)
                    toProductDocument(product, categoryIds)
                }
            }

            productDocuments.chunked(100).forEach { batch ->
                productDocumentService.bulkIndex(batch)
            }

        } catch (e: Exception) {
        }
    }

    private fun toProductDocument(product: Product, categoryIds: List<UUID>): ProductDocument {
        return ProductDocument(
            id = product.id!!,
            name = product.name,
            description = product.description,
            brandId = product.brand.id!!,
            brandName = product.brand.name,
            price = product.price,
            status = product.status.name,
            isActive = product.isActive,
            imageUrl = product.imageUrl,
            specifications = product.specifications,
            categoryIds = categoryIds,
            createdAt = product.createdDate,
            updatedAt = product.updatedDate,
            deleted = product.deleted
        )
    }
}