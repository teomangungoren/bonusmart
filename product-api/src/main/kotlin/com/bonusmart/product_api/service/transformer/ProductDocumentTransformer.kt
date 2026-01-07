package com.bonusmart.product_api.service.transformer

import com.bonusmart.product_api.persistence.document.ProductDocument
import com.bonusmart.product_api.persistence.entity.Product
import com.bonusmart.product_api.service.event.ProductEvent
import com.bonusmart.product_api.service.util.PriceDecoder
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.util.UUID

class ProductDocumentTransformer(
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(ProductDocumentTransformer::class.java)

    fun transform(
        event: ProductEvent,
        product: Product,
        categoryIds: List<UUID>
    ): ProductDocument {
        logger.debug("Transforming product event to document: ${event.id}")

        val brandId = event.brandId ?: product.brand.id
            ?: throw IllegalStateException("Brand ID is required for product ${event.id}")

        val price = event.price?.let { PriceDecoder.parsePrice(it) }
            ?: product.price

        val specifications = parseSpecifications(event.specifications) ?: product.specifications

        return ProductDocument(
            id = event.id,
            name = event.name ?: product.name,
            description = event.description ?: product.description,
            brandId = brandId,
            brandName = product.brand.name,
            price = price,
            status = event.status ?: product.status.toString(),
            isActive = event.isActive ?: product.isActive,
            imageUrl = event.imageUrl ?: product.imageUrl,
            specifications = specifications,
            categoryIds = categoryIds,
            createdAt = event.createdAt ?: product.createdDate,
            updatedAt = event.updatedAt ?: product.updatedDate,
            deleted = event.deleted
        )
    }

    private fun parseSpecifications(specNode: com.fasterxml.jackson.databind.JsonNode?): Map<String, Any>? {
        return specNode?.let {
            try {
                objectMapper.convertValue(it, Map::class.java) as? Map<String, Any>
            } catch (e: Exception) {
                logger.warn("Failed to parse specifications: ${e.message}")
                null
            }
        }
    }
}

