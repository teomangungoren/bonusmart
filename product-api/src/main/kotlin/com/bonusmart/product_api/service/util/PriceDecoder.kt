package com.bonusmart.product_api.service.util

import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.Base64

object PriceDecoder {
    private val logger = LoggerFactory.getLogger(PriceDecoder::class.java)

    fun decodeBase64Price(encodedPrice: String): BigDecimal? {
        return try {
            val bytes = Base64.getDecoder().decode(encodedPrice)
            BigDecimal(java.math.BigInteger(bytes), 2)
        } catch (e: Exception) {
            logger.warn("Failed to decode Base64 price: ${e.message}")
            null
        }
    }

    fun parsePrice(priceString: String?): BigDecimal? {
        if (priceString.isNullOrBlank()) return null
        
        return try {
            decodeBase64Price(priceString) ?: BigDecimal(priceString)
        } catch (e: Exception) {
            logger.warn("Failed to parse price from string: $priceString, error: ${e.message}")
            null
        }
    }

    fun parsePrice(priceNode: com.fasterxml.jackson.databind.JsonNode?): BigDecimal? {
        return when {
            priceNode == null || priceNode.isNull -> null
            priceNode.isTextual -> parsePrice(priceNode.asText())
            else -> try {
                BigDecimal(priceNode.asText())
            } catch (e: Exception) {
                logger.warn("Failed to parse price: ${e.message}")
                null
            }
        }
    }
}

