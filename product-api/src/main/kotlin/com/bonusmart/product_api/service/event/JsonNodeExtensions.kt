package com.bonusmart.product_api.service.event

import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.Instant
import java.util.Base64
import java.util.UUID

private val logger = LoggerFactory.getLogger("JsonNodeExtensions")

fun JsonNode.text(fieldName: String, default: String? = null): String? {
    return getField(fieldName)?.asTextOrNull() ?: default
}

fun JsonNode.uuid(fieldName: String): UUID? {
    return getField(fieldName)?.asTextOrNull()?.let {
        try {
            UUID.fromString(it)
        } catch (e: Exception) {
            logger.warn("Invalid UUID format for field $fieldName: $it")
            null
        }
    }
}

fun JsonNode.price(fieldName: String): BigDecimal? {
    val priceNode = getField(fieldName) ?: return null
    
    return when {
        priceNode.isNull -> null
        priceNode.isTextual -> {
            val priceString = priceNode.asText()
            try {
                val bytes = Base64.getDecoder().decode(priceString)
                BigDecimal(java.math.BigInteger(bytes), 2)
            } catch (e: Exception) {
                try {
                    BigDecimal(priceString)
                } catch (e2: Exception) {
                    logger.warn("Failed to parse price: ${e2.message}")
                    null
                }
            }
        }
        else -> {
            try {
                BigDecimal(priceNode.asText())
            } catch (e: Exception) {
                logger.warn("Failed to parse price: ${e.message}")
                null
            }
        }
    }
}

fun JsonNode.bool(fieldName: String, default: Boolean = false): Boolean {
    return getField(fieldName)?.asBooleanOrNull() ?: default
}

fun JsonNode.instant(fieldName: String): Instant? {
    return getField(fieldName)?.asTextOrNull()?.let {
        try {
            Instant.parse(it)
        } catch (e: Exception) {
            logger.warn("Failed to parse instant for field $fieldName: $it")
            null
        }
    }
}

private fun JsonNode.getField(fieldName: String): JsonNode? {
    return get(fieldName)?.takeIf { !it.isNull }
}

private fun JsonNode.asTextOrNull(): String? {
    return takeIf { !isNull && isTextual }?.asText()
}

private fun JsonNode.asBooleanOrNull(): Boolean? {
    return takeIf { !isNull && isBoolean }?.asBoolean()
}

