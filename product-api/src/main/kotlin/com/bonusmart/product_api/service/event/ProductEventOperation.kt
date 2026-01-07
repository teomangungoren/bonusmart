package com.bonusmart.product_api.service.event

enum class ProductEventOperation(val code: String) {
    CREATE("c"),
    READ("r"),
    UPDATE("u"),
    DELETE("d"),
    UNKNOWN("");

    companion object {
        fun fromCode(code: String?): ProductEventOperation {
            return values().find { it.code == code } ?: UNKNOWN
        }
    }
}

