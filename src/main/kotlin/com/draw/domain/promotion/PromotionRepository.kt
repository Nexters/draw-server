package com.draw.domain.promotion

import com.draw.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface PromotionRepository : JpaRepository<Promotion, Long> {
    fun findAllByUserAndConsumedAtIsNull(user: User): List<Promotion>
}
