package com.bonusmart.product_api.domain.dto

import com.fasterxml.jackson.databind.JsonNode
import java.util.UUID

data class DebeziumProductEvent(
    val id: UUID,
    val op: Operation,
    val deleted: Boolean,
    val payload: JsonNode
)
enum class Operation {
    CREATE, UPDATE, DELETE, SNAPSHOT;

    companion object {
        fun from(value: String?): Operation =
            when (value) {
                "c" -> CREATE
                "u" -> UPDATE
                "d" -> DELETE
                "r" -> SNAPSHOT
                else -> UPDATE
            }
    }
}