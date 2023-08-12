package com.draw.domain.promotion

import com.draw.common.enums.PromotionType
import com.draw.domain.user.User
import org.springframework.stereotype.Component
import java.time.ZonedDateTime

@Component
class AttendancePromotionGenerator {
    fun generate(user: User): List<Promotion> {
        if (user.lastLoggedAt == null || user.lastLoggedAt!!.isBeforeToday()) {
            return listOf(Promotion(user = user, promotionType = PromotionType.DAILY_ATTENDANCE))
        }
        return emptyList()
    }

    private fun ZonedDateTime.isBeforeToday(): Boolean {
        return this.toLocalDate().isBefore(ZonedDateTime.now().toLocalDate())
    }
}
