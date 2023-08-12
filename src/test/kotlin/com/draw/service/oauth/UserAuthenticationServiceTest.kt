package com.draw.service.oauth

import com.draw.component.JwtProvider
import com.draw.domain.promotion.AttendancePromotionGenerator
import com.draw.domain.user.User
import com.draw.infra.persistence.user.UserRepository
import com.draw.service.promotion.PromotionService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.time.ZonedDateTime

class UserAuthenticationServiceTest {

    private val jwtProvider = mockk<JwtProvider>()
    private val userRepository = mockk<UserRepository>()
    private val promotionService = mockk<PromotionService>(relaxed = true)
    private val attendancePromotionGenerator = mockk<AttendancePromotionGenerator>(relaxed = true)

    private val sut = UserAuthenticationService(userRepository, jwtProvider, attendancePromotionGenerator, promotionService)

    @Test
    fun `유저 인증이 완료되면 마지막 로그인 시간을 갱신한다`() {
        val yesterday = ZonedDateTime.now().minusDays(1L)
        val user = User(lastLoggedAt = yesterday)
        every { jwtProvider.authenticate(any()) } returns UsernamePasswordAuthenticationToken(user, null)
        every { userRepository.save(any()) } returns User()
        val actual = sut.authenticate("token")
        val principal = actual.principal as User
        assertTrue(principal.lastLoggedAt != yesterday)
    }
}
