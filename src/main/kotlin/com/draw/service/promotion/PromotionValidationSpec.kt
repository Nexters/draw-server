package com.draw.service.promotion

import com.draw.domain.promotion.Promotion
import com.draw.domain.user.User
import org.springframework.stereotype.Component

@Component
class PromotionValidationSpec {
    fun validate(user: User, promotion: Promotion) {
        require(user.id == promotion.user.id) { "유저가 해당 프로모션의 소유자가 아닙니다" }
        require(!promotion.isConsumed()) { "이미 사용된 프로모션입니다. id = ${promotion.id}" }
    }
}
