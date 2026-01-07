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
    fun updateBrand(brandId: UUID, request: UpdateBrandRequest): BrandResponse {
        val brand = brandService.retrieveBrandById(brandId)
        
        request.name?.let { brand.name = it }
        request.description?.let { brand.description = it }
        request.slug?.let { brand.slug = it }
        request.logoUrl?.let { brand.logoUrl = it }
        request.isActive?.let { brand.isActive = it }
        
        val updated = brandService.updateBrand(brand)
        return BrandResponse.from(updated)
    }
    
    @Transactional(readOnly = true)
    fun retrieveBrandById(brandId: UUID): BrandResponse {
        val brand = brandService.retrieveBrandById(brandId)
        return BrandResponse.from(brand)
    }
    
    @Transactional(readOnly = true)
    fun retrieveAllBrands(): List<BrandResponse> {
        return brandService.retrieveAllBrands()
            .map { BrandResponse.from(it) }
    }
    
    @Transactional
    fun deleteBrand(brandId: UUID) {
        brandService.deleteBrand(brandId)
    }
}



