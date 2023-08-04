package com.draw.domain.promotion

import com.draw.common.enums.PromotionType
import com.draw.domain.common.BaseEntity
import com.draw.domain.user.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.ZonedDateTime

@Entity
class Promotion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    val user: User,

    val promotionType: PromotionType,

    val consumedAt: ZonedDateTime? = null,
) : BaseEntity()
