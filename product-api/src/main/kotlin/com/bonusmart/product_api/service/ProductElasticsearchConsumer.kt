package com.bonusmart.product_api.service

import com.bonusmart.product_api.persistence.document.ProductDocument
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Service
class ProductElasticsearchConsumer(
    private val objectMapper: ObjectMapper,
    private val productService: ProductService,
    private val productCategoryService: ProductCategoryService,
    private val productDocumentService: ProductDocumentService
) {
    private val logger = LoggerFactory.getLogger(ProductElasticsearchConsumer::class.java)

    @KafkaListener(
        topics = ["product-api.public.products"],
        groupId = "product-elasticsearch-sync",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    fun consumeDebeziumEvent(
        @Payload message: String,
        @Header(KafkaHeaders.RECEIVED_KEY) key: String?,
        acknowledgment: Acknowledgment
    ) {
        logger.info("Received Kafka message. Key: $key, Message length: ${message.length}")

        try {
            if (message.isBlank()) {
                logger.warn("Received empty message, skipping")
                acknowledgment.acknowledge()
                return
            }

            logger.debug("Parsing JSON message...")
            val event = try {
                objectMapper.readTree(message)
            } catch (e: Exception) {
                logger.error("Failed to parse message as JSON. Message: $message", e)
                acknowledgment.acknowledge()
                return
            }

            if (event == null || !event.isObject) {
                logger.warn("Event is not an object, skipping. Message: $message")
                acknowledgment.acknowledge()
                return
            }

            logger.debug("Processing event. Keys: ${event.fieldNames().asSequence().toList()}")

            val idNode = event.get("id")
            if (idNode == null || idNode.isNull || !idNode.isTextual) {
                logger.warn("No valid id field found, skipping. Message: $message")
                acknowledgment.acknowledge()
                return
            }

            val productId = try {
                UUID.fromString(idNode.asText())
            } catch (e: Exception) {
                logger.error("Invalid UUID format: ${idNode.asText()}", e)
                acknowledgment.acknowledge()
                return
            }

            val opNode = event.get("__op")
            val op = opNode?.takeIf { !it.isNull && it.isTextual }?.asText()

            val deletedNode = event.get("__deleted")
            val isDeleted = deletedNode?.asText() == "true"

            logger.info("Processing product $productId - op: $op, deleted: $isDeleted")

            when {
                op == "d" || isDeleted -> {
                    handleDelete(productId)
                }
                op == "c" || op == "r" -> {
                    handleCreate(event, productId)
                }
                op == "u" -> {
                    handleUpdate(event, productId)
                }
                op == null -> {
                    if (productDocumentService.existsById(productId)) {
                        handleUpdate(event, productId)
                    } else {
                        handleCreate(event, productId)
                    }
                }
                else -> {
                    logger.warn("Unknown operation type: $op, skipping")
                }
            }

            acknowledgment.acknowledge()
            logger.info("Successfully processed product $productId")

        } catch (e: Exception) {
            logger.error("Unexpected error processing Debezium event. Message: $message", e)
            acknowledgment.acknowledge()
        }
    }

    private fun handleCreate(data: JsonNode, productId: UUID) {
        logger.info("Creating product document: $productId")

        if (productDocumentService.existsById(productId)) {
            logger.info("Product $productId already exists, skipping create")
            return
        }

        val productDocument = transformToDocument(data, productId)
        productDocumentService.indexProduct(productDocument)
        logger.info("Successfully created product document: $productId")
    }

    private fun handleUpdate(data: JsonNode, productId: UUID) {
        logger.info("Updating product document: $productId")

        val productDocument = transformToDocument(data, productId)
        productDocumentService.updateProduct(productDocument)
        logger.info("Successfully updated product document: $productId")
    }

    private fun handleDelete(productId: UUID) {
        logger.info("Deleting product document: $productId")

        productDocumentService.deleteProduct(productId)
        logger.info("Successfully deleted product document: $productId")
    }

    private fun transformToDocument(data: JsonNode, productId: UUID): ProductDocument {
        logger.debug("Transforming data to ProductDocument for product: $productId")

        val product = productService.retrieveProductById(productId)
        val categoryIds = productCategoryService.getCategoryIdsForProduct(productId)

        val brandIdNode = data.get("brand_id") ?: data.get("brandId")
        val brandId = if (brandIdNode != null && !brandIdNode.isNull && brandIdNode.isTextual) {
            UUID.fromString(brandIdNode.asText())
        } else {
            product.brand.id
        }

        return ProductDocument(
            id = productId,
            name = data.get("name")?.asText() ?: product.name,
            description = data.get("description")?.asText() ?: product.description,
            brandId = brandId!!,
            brandName = product.brand.name,
            price = data.get("price")?.let {
                if (it.isTextual) {
                    try {
                        val bytes = java.util.Base64.getDecoder().decode(it.asText())
                        BigDecimal(java.math.BigInteger(bytes), 2)
                    } catch (e: Exception) {
                        logger.warn("Failed to decode price, using existing: ${e.message}")
                        product.price
                    }
                } else {
                    BigDecimal(it.asText())
                }
            } ?: product.price,
            status = (data.get("status")?.asText() ?: product.status).toString(),
            isActive = data.get("is_active")?.asBoolean()
                ?: data.get("isActive")?.asBoolean()
                ?: product.isActive,
            imageUrl = data.get("image_url")?.asText()
                ?: data.get("imageUrl")?.asText()
                ?: product.imageUrl,
            specifications = parseSpecifications(data.get("specifications"))
                ?: product.specifications,
            categoryIds = categoryIds,
            createdAt = parseInstant(data.get("created_date") ?: data.get("createdDate"))
                ?: product.createdDate,
            updatedAt = parseInstant(data.get("updated_date") ?: data.get("updatedDate"))
                ?: product.updatedDate,
            deleted = data.get("deleted")?.asBoolean() ?: false
        )
    }

    private fun parseSpecifications(specNode: JsonNode?): Map<String, Any>? {
        return specNode?.let {
            try {
                objectMapper.convertValue(it, Map::class.java) as? Map<String, Any>
            } catch (e: Exception) {
                logger.warn("Failed to parse specifications: ${e.message}")
                null
            }
        }
    }

    private fun parseInstant(dateNode: JsonNode?): Instant? {
        return dateNode?.asText()?.let {
            try {
                Instant.parse(it)
            } catch (e: Exception) {
                logger.warn("Failed to parse date: ${e.message}")
                null
            }
        }
    }
}