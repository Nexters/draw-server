package com.draw.service.promotion

import com.draw.domain.promotion.Promotion
import com.draw.domain.promotion.PromotionRepository
import com.draw.domain.user.User
import com.draw.infra.persistence.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PromotionService(
    private val promotionRepository: PromotionRepository,
    private val userRepository: UserRepository,
    private val spec: PromotionValidationSpec,
) {
    fun listAllUnconsumedPromotions(user: User): List<Promotion> {
        return promotionRepository.findAllByUserAndConsumedAtIsNull(user)
    }

    @Transactional
    fun grant(promotions: List<Promotion>): List<Promotion> {
        return promotionRepository.saveAll(promotions)
    }

    @Transactional
    fun consume(user: User, promotionId: Long) {
        val promotion = promotionRepository.findById(promotionId).orElseThrow {
            throw IllegalArgumentException("존재하지 않는 프로모션 입니다. id = $promotionId")
        }
        spec.validate(user, promotion)

        val result = promotion.toResult()
        promotion.consume()
        promotion.user.grantPoint(result.toBePoint)
        userRepository.save(promotion.user)
        promotionRepository.save(promotion)
    }
}
