package com.bonusmart.product_api.domain.response

import java.math.BigDecimal
import java.util.UUID

data class ProductSearchResponse(
    val products: List<ProductResponse>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int,
    val pageSize: Int,
    val facets: SearchFacets,
    val suggestions: List<String>? = null,
    val sortOptions: List<SortOption> = listOf(
        SortOption("relevance", "En Uygun"),
        SortOption("price_asc", "Fiyat: Düşükten Yükseğe"),
        SortOption("price_desc", "Fiyat: Yüksekten Düşüğe"),
        SortOption("newest", "En Yeni"),
        SortOption("oldest", "En Eski")
    )
)

data class SearchFacets(
    val categories: List<FacetItem>,
    val brands: List<FacetItem>,
    val priceRanges: List<PriceRangeFacet>,
    val statuses: List<FacetItem>
)

data class FacetItem(
    val id: UUID,
    val name: String,
    val count: Long
)

data class PriceRangeFacet(
    val min: BigDecimal,
    val max: BigDecimal?,
    val count: Long
)

data class SortOption(
    val value: String,
    val label: String
)




