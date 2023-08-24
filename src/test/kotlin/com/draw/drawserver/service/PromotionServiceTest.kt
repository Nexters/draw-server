package com.draw.drawserver.service

import com.draw.common.enums.OAuthProvider
import com.draw.common.enums.PromotionType
import com.draw.domain.promotion.Promotion
import com.draw.domain.promotion.PromotionRepository
import com.draw.domain.user.User
import com.draw.infra.persistence.user.UserRepository
import com.draw.service.promotion.PromotionService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class PromotionServiceTest {

    @Autowired
    private lateinit var sut: PromotionService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var promotionRepository: PromotionRepository

    @Nested
    inner class ConsumeContext {
        @Test
        fun `프로모션을 사용처리하고 유저에게 프로모션 지급액을 지급한다`() {
            val user = userRepository.save(User(oauthProvider = OAuthProvider.KAKAO))
            val promotion = promotionRepository.save(Promotion(user = user, promotionType = PromotionType.WELCOME))
            sut.consume(user, promotion.id)
            assertTrue(promotion.isConsumed())
            assertEquals(PromotionType.WELCOME.grantPoint, user.getPoint())
        }

        @Test
        fun `이미 사용된 프로모션을 재사용하려고 시도하면 예외가 발생한다`() {
            val user = userRepository.save(User(oauthProvider = OAuthProvider.KAKAO))
            val promotion = promotionRepository.save(Promotion(user = user, promotionType = PromotionType.WELCOME))
            sut.consume(user, promotion.id)
            assertThrows<IllegalArgumentException> {
                sut.consume(user, promotion.id)
            }
        }
    }

    @Test
    fun `유저의 사용하지 않은 모든 프로모션을 반환한다`() {
        val user = userRepository.save(User(oauthProvider = OAuthProvider.KAKAO))
        val promotions = promotionRepository.save(Promotion(user = user, promotionType = PromotionType.WELCOME))
        val actual = sut.listAllUnconsumedPromotions(user)
        assertEquals(actual.size, 1)
        assertEquals(actual.first(), promotions)
    }

    @Test
    fun `사용한 프로모션은 반환되지 않는다`() {
        val user = userRepository.save(User(oauthProvider = OAuthProvider.KAKAO))
        val consumedPromotion =
            Promotion(user = user, promotionType = PromotionType.DAILY_ATTENDANCE).apply { consume() }
        promotionRepository.saveAll(
            listOf(
                Promotion(user = user, promotionType = PromotionType.WELCOME),
                consumedPromotion,
            ),
        )
        val actual = sut.listAllUnconsumedPromotions(user)
        assertEquals(actual.size, 1)
        assertTrue(actual.all { !it.isConsumed() })
    }
}
