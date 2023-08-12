package com.draw.domain.promotion

import com.draw.common.enums.PromotionType
import com.draw.domain.user.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class AttendancePromotionGeneratorTest {

    @Test
    fun `유저가 오늘 첫 접속이라면 출석체크 프로모션을 발급한다`() {
        val generator = AttendancePromotionGenerator()
        val user = User(lastLoggedAt = ZonedDateTime.now().minusDays(1L))
        val actual = generator.generate(user)
        assertEquals(actual.size, 1)
        assertEquals(actual[0].promotionType, PromotionType.DAILY_ATTENDANCE)
    }
}
