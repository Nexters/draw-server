package com.draw.drawserver.service

import com.draw.common.enums.PromotionType
import com.draw.domain.promotion.Promotion
import com.draw.domain.user.User
import com.draw.service.promotion.PromotionRenderer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PromotionTest {
    @Test
    fun `promotion 을 구체객체로 변환한다`() {
        val promotion = Promotion(user = User(point = 0L), promotionType = PromotionType.WELCOME)
        val promotionRenderer = PromotionRenderer()
        val rendered = promotionRenderer.render(promotion)
        assertEquals(rendered.title, "웰컴 선물이에요!")
        assertEquals(rendered.grantedPoint, 500L)
        assertEquals(rendered.asIsPoint, 0L)
        assertEquals(rendered.toBePoint, 500L)
        /**
         * title, grantedPoint, asIsPoint, toBePoint
         */
    }
}
