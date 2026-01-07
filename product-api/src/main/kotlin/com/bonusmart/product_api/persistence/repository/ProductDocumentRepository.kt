package com.bonusmart.product_api.persistence.repository

import com.bonusmart.product_api.persistence.document.ProductDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import java.util.UUID

interface ProductDocumentRepository : ElasticsearchRepository<ProductDocument,UUID> {

}