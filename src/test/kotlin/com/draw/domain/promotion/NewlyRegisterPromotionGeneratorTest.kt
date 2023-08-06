package com.draw.domain.promotion

import com.draw.common.enums.PromotionType
import com.draw.domain.user.User
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NewlyRegisterPromotionGeneratorTest {

    @Test
    fun `신규회원에 대해서 신규가입 프로모션들을 발급한다`() {
        val generator = NewlyRegisterPromotionGenerator()
        val user = User()
        val actual = generator.generate(user)
        val expected =
            Promotion(user = user, promotionType = PromotionType.WELCOME)
        assertTrue(actual.any { it.isSame(expected) })
    }

    private fun Promotion.isSame(other: Promotion): Boolean {
        return this.user == other.user && this.promotionType == other.promotionType
    }
}
