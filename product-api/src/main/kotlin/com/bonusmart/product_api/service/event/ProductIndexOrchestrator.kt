package com.bonusmart.product_api.service.event

import com.bonusmart.product_api.domain.dto.DebeziumProductEvent
import com.bonusmart.product_api.domain.dto.Operation
import com.bonusmart.product_api.service.ProductDocumentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductIndexOrchestrator(
    private val productDocumentService: ProductDocumentService,
    private val productDocumentMapper: ProductDocumentMapper
) {
    private val logger = LoggerFactory.getLogger(ProductIndexOrchestrator::class.java)

    fun handle(event: DebeziumProductEvent) {
        when {
            shouldDelete(event) -> deleteProduct(event)
            shouldUpdate(event) -> updateProduct(event)
            else -> indexProduct(event)
        }
    }

    private fun shouldDelete(event: DebeziumProductEvent): Boolean {
        return event.op == Operation.DELETE || event.deleted
    }

    private fun shouldUpdate(event: DebeziumProductEvent): Boolean {
        return productDocumentService.existsById(event.id)
    }

    private fun deleteProduct(event: DebeziumProductEvent) {
        logger.info("Deleting product document: ${event.id}")
        productDocumentService.deleteProduct(event.id)
    }

    private fun updateProduct(event: DebeziumProductEvent) {
        logger.info("Updating product document: ${event.id}")
        productDocumentService.updateProduct(
            productDocumentMapper.toDocument(event)
        )
    }

    private fun indexProduct(event: DebeziumProductEvent) {
        logger.info("Indexing product document: ${event.id}")
        productDocumentService.indexProduct(
            productDocumentMapper.toDocument(event)
        )
    }
}
