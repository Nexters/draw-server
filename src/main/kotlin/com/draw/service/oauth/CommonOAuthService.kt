package com.draw.service.oauth

import com.draw.common.RefreshTokenExpiredException
import com.draw.component.JwtProvider
import com.draw.domain.user.User
import com.draw.infra.persistence.user.UserRepository
import com.draw.service.oauth.dto.TokenRefreshResult
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CommonOAuthService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
) {
    fun refreshToken(accessToken: String, refreshToken: String): TokenRefreshResult {
        val user = extractUserFromAccessToken(accessToken)

        if (user.refreshToken != refreshToken) {
            throw IllegalStateException("전달받은 리프레시 토큰이 실제 유저의 리프레시 토큰과 일치하지 않습니다.")
        }
        jwtProvider.validate(user, refreshToken)

        val newAccessToken = jwtProvider.generateAccessToken(user)
        val newRefreshToken = jwtProvider.generateRefreshToken(user)
        userRepository.save(user.apply { this.refreshToken = newRefreshToken })
        return TokenRefreshResult(accessToken = newAccessToken, refreshToken = newRefreshToken)
    }

    private fun extractUserFromAccessToken(accessToken: String): User {
        val userIdInToken = runCatching {
            jwtProvider.getId(accessToken).toLong()
        }.onFailure { e ->
            when (e) {
                is ExpiredJwtException -> throw RefreshTokenExpiredException()
                else -> throw Exception(e.message)
            }
        }.getOrThrow()

        return userRepository.findById(userIdInToken).getOrNull()
            ?: throw IllegalArgumentException("토큰에 해당하는 유저가 존재하지않습니다")
    }
}
