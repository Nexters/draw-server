package com.draw.service.oauth

import com.draw.component.JwtProvider
import com.draw.domain.promotion.AttendancePromotionGenerator
import com.draw.domain.user.User
import com.draw.infra.persistence.user.UserRepository
import com.draw.service.promotion.PromotionService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class UserAuthenticationService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val attendancePromotionGenerator: AttendancePromotionGenerator,
    private val promotionService: PromotionService,
) {
    @Transactional
    fun authenticate(token: String): Authentication {
        val authentication = jwtProvider.authenticate(token)
        val user = authentication.principal as User
        promotionService.grant(attendancePromotionGenerator.generate(user))
        user.lastLoggedAt = ZonedDateTime.now()
        userRepository.save(user)
        return authentication
    }
}
