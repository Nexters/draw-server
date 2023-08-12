package com.draw.common.enums

import com.draw.domain.Point
import com.draw.domain.promotion.Promotion
import com.draw.domain.user.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PromotionTypeTest {
    @Test
    fun `user 를 입력으로 받아서 프로모션 결과 객체를 반환한다`() {
        val promotion = Promotion(user = User(point = 0L), promotionType = PromotionType.WELCOME)
        val result = promotion.promotionType.toResult(promotion.user)
        assertEquals(result.title, "웰컴 선물이에요!")
        assertEquals(result.grantedPoint, Point(500L))
        assertEquals(result.asIsPoint, Point(0L))
        assertEquals(result.toBePoint, Point(500L))
    }
}
