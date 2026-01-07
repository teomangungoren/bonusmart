package com.bonusmart.product_api.service

import com.bonusmart.product_api.persistence.entity.Brand
import com.bonusmart.product_api.persistence.repository.BrandRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BrandService(
    private val brandRepository: BrandRepository
) {
    
    @Transactional(readOnly = true)
    fun retrieveBrandById(brandId: UUID): Brand {
        return brandRepository.findById(brandId)
            .filter { !it.deleted }
            .orElseThrow { IllegalArgumentException("Brand not found or deleted: $brandId") }
    }
    
    @Transactional(readOnly = true)
    fun retrieveAllBrands(): List<Brand> {
        return brandRepository.findAllByDeletedFalse()
    }
    
    @Transactional
    fun createBrand(brand: Brand): Brand {
        brandRepository.findByNameAndDeletedFalse(brand.name)?.let {
            throw IllegalArgumentException("Brand with name '${brand.name}' already exists")
        }
        return brandRepository.save(brand)
    }
    
    @Transactional
    fun updateBrand(brand: Brand): Brand {
        retrieveBrandById(brand.id ?: throw IllegalArgumentException("Brand ID is required"))
        return brandRepository.save(brand)
    }
    
    @Transactional
    fun deleteBrand(brandId: UUID) {
        retrieveBrandById(brandId)
            .apply { deleted = true }
            .let { brandRepository.save(it) }
    }
    
    @Transactional(readOnly = true)
    fun existsByIdAndNotDeleted(brandId: UUID): Boolean {
        return brandRepository.findById(brandId)
            .filter { !it.deleted }
            .isPresent
    }
    
    @Transactional(readOnly = true)
    fun getBrandNameById(brandId: UUID): String {
        return brandRepository.findBrandNameById(brandId)
            ?: throw IllegalArgumentException("Brand not found or deleted: $brandId")
    }
}

