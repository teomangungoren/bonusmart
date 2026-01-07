package com.bonusmart.product_api.persistence.entity

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseAuditorAware(

    @CreatedDate
    var createdDate: Instant? = null,

    @CreatedBy
    var createdBy: String? = null,

    @LastModifiedDate
    var updatedDate: Instant? = null,

    @LastModifiedBy
    var updatedBy: String? = null
)
