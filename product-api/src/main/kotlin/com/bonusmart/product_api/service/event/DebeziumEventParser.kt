package com.bonusmart.product_api.service.event

import com.bonusmart.product_api.domain.dto.DebeziumProductEvent
import com.bonusmart.product_api.domain.dto.Operation
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DebeziumEventParser(
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(DebeziumEventParser::class.java)

    fun parse(message: String): DebeziumProductEvent? {
        if (message.isBlank()) {
            logger.warn("Received empty message, skipping")
            return null
        }

        return try {
            val jsonNode = objectMapper.readTree(message)
            
            if (!jsonNode.isObject) {
                logger.warn("Event is not a valid JSON object, skipping")
                return null
            }

            val idNode = jsonNode.get("id")
            if (idNode == null || idNode.isNull || !idNode.isTextual) {
                logger.warn("No valid id field found, skipping")
                return null
            }

            val productId = try {
                UUID.fromString(idNode.asText())
            } catch (e: Exception) {
                logger.error("Invalid UUID format: ${idNode.asText()}", e)
                return null
            }

            val opNode = jsonNode.get("__op")
            val op = Operation.from(opNode?.takeIf { !it.isNull && it.isTextual }?.asText())

            val deletedNode = jsonNode.get("__deleted")
            val isDeleted = deletedNode?.takeIf { !it.isNull && it.isTextual }?.asText() == "true"

            DebeziumProductEvent(
                id = productId,
                op = op,
                deleted = isDeleted,
                payload = jsonNode
            )
        } catch (e: Exception) {
            logger.error("Failed to parse message as JSON", e)
            null
        }
    }
}
