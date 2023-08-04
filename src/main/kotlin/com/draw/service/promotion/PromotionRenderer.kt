package com.draw.service.promotion

import com.draw.domain.promotion.Promotion

class PromotionRenderer {
    fun render(promotion: Promotion): RenderedPromotion {
        return RenderedPromotion("웰컴 선물이에요!", 500L, 0L, 500L)
    }

    data class RenderedPromotion(
        val title: String,
        val grantedPoint: Long,
        val asIsPoint: Long,
        val toBePoint: Long,
    )
}
