package com.bonusmart.product_api.service

import com.bonusmart.product_api.domain.response.*
import com.bonusmart.product_api.persistence.document.ProductDocument
import com.bonusmart.product_api.persistence.repository.ProductDocumentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.Criteria
import org.springframework.data.elasticsearch.core.query.CriteriaQuery
import org.springframework.data.elasticsearch.core.query.Query
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class ProductSearchService(
    private val productDocumentRepository: ProductDocumentRepository,
    private val elasticsearchOperations: ElasticsearchOperations
) {

    fun searchProducts(
        query: String?,
        categoryIds: List<UUID>? = null,
        brandIds: List<UUID>? = null,
        status: String? = null,
        minPrice: BigDecimal? = null,
        maxPrice: BigDecimal? = null,
        specifications: Map<String, Any>? = null,
        sortBy: String = "relevance",
        page: Int = 0,
        size: Int = 20
    ): ProductSearchResponse {
        val criteria = Criteria("deleted").`is`(false)
            .and(Criteria("isActive").`is`(true))

        query?.takeIf { it.isNotBlank() }?.let { searchQuery ->
            criteria.and(
                Criteria("name").matches(searchQuery).boost(5.0F)
                    .or(Criteria("description").matches(searchQuery).boost(2.0F))
                    .or(Criteria("brandName").matches(searchQuery).boost(3.0F))
            )
        }

        categoryIds?.takeIf { it.isNotEmpty() }?.let {
            criteria.and(Criteria("categoryIds").`in`(it.map { id -> id.toString() }))
        }

        brandIds?.takeIf { it.isNotEmpty() }?.let {
            criteria.and(Criteria("brandId").`in`(it.map { id -> id.toString() }))
        }

        status?.let {
            criteria.and(Criteria("status").`is`(it))
        }

        if (minPrice != null || maxPrice != null) {
            val priceCriteria = Criteria("price")
            minPrice?.let { priceCriteria.greaterThanEqual(it) }
            maxPrice?.let { priceCriteria.lessThanEqual(it) }
            criteria.and(priceCriteria)
        }

        val sort = when (sortBy) {
            "price_asc" -> Sort.by(Sort.Order.asc("price"))
            "price_desc" -> Sort.by(Sort.Order.desc("price"))
            "newest" -> Sort.by(Sort.Order.desc("createdAt"))
            "oldest" -> Sort.by(Sort.Order.asc("createdAt"))
            else -> Sort.by(Sort.Order.desc("_score"))
        }

        val queryBuilder = CriteriaQuery.builder(criteria)
            .withPageable(PageRequest.of(page, size))
            .withSort(sort)
            .build()

        val searchHits = elasticsearchOperations.search(queryBuilder as Query, ProductDocument::class.java)

        val products = searchHits.searchHits.map { it.content }
            .map { ProductResponse.fromDocument(it) }

        val facets = buildFacets()

        return ProductSearchResponse(
            products = products,
            totalElements = searchHits.totalHits,
            totalPages = (searchHits.totalHits / size).toInt() + if (searchHits.totalHits % size > 0) 1 else 0,
            currentPage = page,
            pageSize = size,
            facets = facets
        )
    }

    private fun buildFacets(): SearchFacets {
        return SearchFacets(
            categories = emptyList(),
            brands = emptyList(),
            priceRanges = emptyList(),
            statuses = emptyList()
        )
    }

    fun getSuggestions(query: String, limit: Int = 10): List<String> {
        val criteria = Criteria("deleted").`is`(false)
            .and(Criteria("isActive").`is`(true))
            .and(Criteria("name").startsWith(query))

        val queryBuilder = CriteriaQuery.builder(criteria)
            .withPageable(PageRequest.of(0, limit))
            .build()

        val searchHits = elasticsearchOperations.search(queryBuilder as Query, ProductDocument::class.java)
        return searchHits.searchHits.map { it.content.name }.distinct()
    }

    fun findById(productId: UUID): ProductDocument? {
        return productDocumentRepository.findById(productId)
            .filter { !it.deleted && it.isActive }
            .orElse(null)
    }

    fun findAll(pageable: Pageable): Page<ProductDocument> {
        val criteria = Criteria("deleted").`is`(false)
            .and(Criteria("isActive").`is`(true))

        val queryBuilder = CriteriaQuery.builder(criteria)
            .withPageable(pageable)
            .build()

        val searchHits = elasticsearchOperations.search(queryBuilder as Query, ProductDocument::class.java)
        return PageImpl(
            searchHits.searchHits.map { it.content },
            pageable,
            searchHits.totalHits
        )
    }

    fun findByCategory(categoryId: UUID, pageable: Pageable): Page<ProductDocument> {
        val criteria = Criteria("deleted").`is`(false)
            .and(Criteria("isActive").`is`(true))
            .and(Criteria("categoryIds").`in`(categoryId.toString()))

        val queryBuilder = CriteriaQuery.builder(criteria)
            .withPageable(pageable)
            .build()

        val searchHits = elasticsearchOperations.search(queryBuilder as Query, ProductDocument::class.java)
        return PageImpl(
            searchHits.searchHits.map { it.content },
            pageable,
            searchHits.totalHits
        )
    }
}

