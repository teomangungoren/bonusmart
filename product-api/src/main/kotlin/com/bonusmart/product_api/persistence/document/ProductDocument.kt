package com.bonusmart.product_api.persistence.document

import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.*
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Document(indexName = "products", createIndex = true)
data class ProductDocument(
    @Id
    val id: UUID,

    @Field(
        type = FieldType.Text,
        analyzer = "standard",
        searchAnalyzer = "standard"
    )
    val name: String,

    @Field(
        type = FieldType.Text,
        analyzer = "standard",
        searchAnalyzer = "standard"
    )
    val description: String?,

    @Field(type = FieldType.Keyword)
    val brandId: UUID,

    @Field(
        type = FieldType.Text,
        analyzer = "standard"
    )
    val brandName: String,

    @Field(type = FieldType.Double)
    val price: BigDecimal,

    @Field(type = FieldType.Keyword)
    val status: String,

    @Field(type = FieldType.Boolean)
    val isActive: Boolean,

    @Field(type = FieldType.Keyword)
    val imageUrl: String?,

    @Field(type = FieldType.Object)
    val specifications: Map<String, Any>?,

    @Field(type = FieldType.Keyword)
    val categoryIds: List<UUID>,

    @Field(type = FieldType.Date, format = [DateFormat.date_time])
    val createdAt: Instant?,

    @Field(type = FieldType.Date, format = [DateFormat.date_time])
    val updatedAt: Instant?,

    @Field(type = FieldType.Boolean)
    val deleted: Boolean = false
)