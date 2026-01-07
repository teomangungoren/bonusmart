package com.bonusmart.product_api.controller

import com.bonusmart.product_api.domain.request.CreateBrandRequest
import com.bonusmart.product_api.domain.request.UpdateBrandRequest
import com.bonusmart.product_api.domain.response.BrandResponse
import com.bonusmart.product_api.service.BrandApplicationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/brands")
class BrandController(
    private val brandApplicationService: BrandApplicationService
) {
    
    @PostMapping
    fun createBrand(
        @RequestBody request: CreateBrandRequest
    ): ResponseEntity<BrandResponse> {
        val brand = brandApplicationService.createBrand(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(brand)
    }
    
    @GetMapping("/{brandId}")
    fun getBrandById(
        @PathVariable brandId: UUID
    ): ResponseEntity<BrandResponse> {
        val brand = brandApplicationService.retrieveBrandById(brandId)
        return ResponseEntity.ok(brand)
    }
    
    @GetMapping
    fun getAllBrands(): ResponseEntity<List<BrandResponse>> {
        val brands = brandApplicationService.retrieveAllBrands()
        return ResponseEntity.ok(brands)
    }
    
    @PutMapping("/{brandId}")
    fun updateBrand(
        @PathVariable brandId: UUID,
        @RequestBody request: UpdateBrandRequest
    ): ResponseEntity<BrandResponse> {
        val brand = brandApplicationService.updateBrand(brandId, request)
        return ResponseEntity.ok(brand)
    }
    
    @DeleteMapping("/{brandId}")
    fun deleteBrand(
        @PathVariable brandId: UUID
    ): ResponseEntity<Void> {
        brandApplicationService.deleteBrand(brandId)
        return ResponseEntity.noContent().build()
    }
}



