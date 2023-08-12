package com.draw.service.oauth

import com.draw.common.enums.OAuthProvider
import com.draw.component.JwtProvider
import com.draw.domain.promotion.NewlyRegisterPromotionGenerator
import com.draw.domain.user.User
import com.draw.infra.external.kakao.KakaoApiClient
import com.draw.infra.external.kakao.KakaoAuthClient
import com.draw.infra.external.kakao.KauthTokenRequest
import com.draw.infra.external.kakao.KauthTokenResponse
import com.draw.infra.persistence.user.UserRepository
import com.draw.properties.KakaoOAuthProperties
import com.draw.service.oauth.dto.LoginResult
import com.draw.service.promotion.PromotionService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class KakaoOAuthService(
    private val kakaoOAuthProperties: KakaoOAuthProperties,
    private val kakaoAuthClient: KakaoAuthClient,
    private val kakaoApiClient: KakaoApiClient,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val newlyRegisterPromotionGenerator: NewlyRegisterPromotionGenerator,
    private val promotionService: PromotionService,
) {
    @Transactional
    fun registerOrLogin(authCode: String, callbackOrigin: String): LoginResult {
        val tokenResponse = fetchKakaoUserAccessToken(authCode, callbackOrigin)
        val userInfo = kakaoApiClient.getUserInfo("Bearer ${tokenResponse.accessToken}")
        val user = userRepository.findByKakaoId(userInfo.id.toString())
        if (user != null) {
            if (!user.registrationCompleted) {
                return LoginResult.newlyRegistered(jwtProvider.generateAccessToken(user), jwtProvider.generateRefreshToken(user))
            }
            return LoginResult.normal(jwtProvider.generateAccessToken(user), jwtProvider.generateRefreshToken(user))
        }
        val newUser = User(kakaoId = userInfo.id.toString(), oauthProvider = OAuthProvider.KAKAO)
        val accessToken = jwtProvider.generateAccessToken(newUser)
        newUser.refreshToken = jwtProvider.generateRefreshToken(newUser)
        userRepository.save(newUser)
        promotionService.grant(newlyRegisterPromotionGenerator.generate(newUser))
        return LoginResult.newlyRegistered(accessToken, newUser.refreshToken!!)
    }

    private fun fetchKakaoUserAccessToken(authCode: String, callbackOrigin: String): KauthTokenResponse {
        return kakaoAuthClient.getToken(
            KauthTokenRequest(
                grantType = "authorization_code",
                clientId = kakaoOAuthProperties.restApiKey,
                redirectUri = "$callbackOrigin/callback/kakao",
                code = authCode,
                clientSecret = kakaoOAuthProperties.clientSecret,
            ).toMap(),
        )
    }
}
