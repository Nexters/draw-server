package com.draw.domain.common

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
open class BaseEntity {
    @CreatedDate
    open var createdAt: ZonedDateTime? = ZonedDateTime.now()

    @LastModifiedDate
    open var updatedAt: ZonedDateTime? = ZonedDateTime.now()
}
