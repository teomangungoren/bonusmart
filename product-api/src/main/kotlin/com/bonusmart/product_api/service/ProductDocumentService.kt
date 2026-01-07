package com.bonusmart.product_api.service

import com.bonusmart.product_api.persistence.document.ProductDocument
import com.bonusmart.product_api.persistence.repository.ProductDocumentRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductDocumentService(private val productDocumentRepository: ProductDocumentRepository) {

    fun indexProduct(productDocument: ProductDocument): ProductDocument {
        return productDocumentRepository.save(productDocument)
    }

    fun updateProduct(productDocument: ProductDocument): ProductDocument {
        return productDocumentRepository.save(productDocument)
    }

    fun deleteProduct(productId: UUID) {
        productDocumentRepository.findById(productId)
            .ifPresent { product ->
                productDocumentRepository.delete(product)
            }
    }

    fun bulkIndex(products: List<ProductDocument>) {
        if (products.isNotEmpty()) {
            productDocumentRepository.saveAll(products)
        }
    }

    fun findById(productId: UUID): ProductDocument? {
        return productDocumentRepository.findById(productId)
            .filter { !it.deleted }
            .orElse(null)
    }

    fun existsById(productId: UUID): Boolean {
        return productDocumentRepository.existsById(productId)
    }
}