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
    var id: Long = 0L,

    @ManyToOne
    val user: User,

    val promotionType: PromotionType,

    var consumedAt: ZonedDateTime? = null,
) : BaseEntity() {
    fun consume() {
        if (consumedAt != null) {
            return
        }
        consumedAt = ZonedDateTime.now()
    }

    fun isConsumed(): Boolean {
        return consumedAt != null
    }

    fun toResult(): PromotionType.Result {
        return promotionType.toResult(user)
    }
}
