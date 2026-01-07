package com.bonusmart.product_api.service.event

import com.bonusmart.product_api.domain.dto.DebeziumProductEvent
import com.bonusmart.product_api.persistence.document.ProductDocument
import com.bonusmart.product_api.service.ProductCategoryService
import com.bonusmart.product_api.service.ProductService
import org.springframework.stereotype.Component

@Component
class ProductDocumentMapper(
    private val productService: ProductService,
    private val productCategoryService: ProductCategoryService
) {

    fun toDocument(event: DebeziumProductEvent): ProductDocument {
        val product = productService.retrieveProductById(event.id)

        return ProductDocument(
            id = product.id!!,
            name = event.payload.text("name", product.name) ?: product.name,
            description = event.payload.text("description", product.description),
            brandId = event.payload.uuid("brand_id") ?: product.brand.id!!,
            brandName = product.brand.name,
            price = event.payload.price("price") ?: product.price,
            status = event.payload.text("status") ?: product.status.toString(),
            isActive = event.payload.bool("is_active", product.isActive),
            imageUrl = event.payload.text("image_url", product.imageUrl),
            specifications = product.specifications,
            categoryIds = productCategoryService.getCategoryIdsForProduct(product.id!!),
            createdAt = event.payload.instant("created_date") ?: product.createdDate,
            updatedAt = event.payload.instant("updated_date") ?: product.updatedDate,
            deleted = event.payload.bool("deleted", false)
        )
    }
}
