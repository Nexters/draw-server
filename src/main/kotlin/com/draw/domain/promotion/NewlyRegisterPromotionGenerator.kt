package com.draw.domain.promotion

import com.draw.common.enums.PromotionType
import com.draw.domain.user.User
import org.springframework.stereotype.Component

@Component
class NewlyRegisterPromotionGenerator {
    fun generate(user: User): List<Promotion> {
        if (!user.isFirstActivity()) {
            return emptyList()
        }
        return listOf(
            Promotion(user = user, promotionType = PromotionType.WELCOME),
        )
    }
}
