package com.bonusmart.product_api.service

import com.bonusmart.product_api.domain.request.CreateBrandRequest
import com.bonusmart.product_api.domain.request.UpdateBrandRequest
import com.bonusmart.product_api.domain.response.BrandResponse
import com.bonusmart.product_api.persistence.entity.Brand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BrandApplicationService(
    private val brandService: BrandService
) {

    @Transactional
    fun createBrand(request: CreateBrandRequest): BrandResponse {
        val brand = Brand(
            name = request.name,
            description = request.description,
            slug = request.slug,
            logoUrl = request.logoUrl,
            isActive = request.isActive ?: true
        )

        val saved = brandService.createBrand(brand)
        return BrandResponse.from(saved)
    }

    @Transactional
    fun updateBrand(brandId: UUID, request: UpdateBrandRequest) {
        brandService.retrieveBrandById(brandId).apply {
            request.name?.let { this.name = it }
            request.description?.let { this.description = it }
            request.slug?.let { this.slug = it }
            request.logoUrl?.let { this.logoUrl = it }
            request.isActive?.let { this.isActive = it }
        }.also {
            brandService.updateBrand(it)
        }
    }

    fun retrieveBrandById(brandId: UUID): BrandResponse {
        val brand = brandService.retrieveBrandById(brandId)
        return BrandResponse.from(brand)
    }

    fun retrieveAllBrands(): List<BrandResponse> {
        return brandService.retrieveAllBrands()
            .map { BrandResponse.from(it) }
    }

    fun deleteBrand(brandId: UUID) {
        brandService.deleteBrand(brandId)
    }
}




