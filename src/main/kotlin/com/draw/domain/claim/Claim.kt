package com.draw.domain.claim

import com.draw.common.enums.ClaimOriginType
import com.draw.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Claim(
    @Column(nullable = false)
    val reportedUserId: Long,

    @Column(nullable = false)
    val informantUserId: Long,

    @Column(nullable = false)
    val originId: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val originType: ClaimOriginType,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
