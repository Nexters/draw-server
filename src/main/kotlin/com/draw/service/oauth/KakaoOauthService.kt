package com.draw.service.oauth

import com.draw.common.enums.OauthProvider
import com.draw.component.JwtProvider
import com.draw.domain.user.User
import com.draw.infra.external.kakao.KakaoApiClient
import com.draw.infra.external.kakao.KakaoAuthClient
import com.draw.infra.external.kakao.KauthTokenRequest
import com.draw.infra.external.kakao.KauthTokenResponse
import com.draw.infra.persistence.user.UserRepository
import com.draw.properties.KakaoOauthProperties
import com.draw.service.oauth.dto.LoginResult
import org.springframework.stereotype.Service

@Service
class KakaoOauthService(
    private val kakaoOauthProperties: KakaoOauthProperties,
    private val kakaoAuthClient: KakaoAuthClient,
    private val kakaoApiClient: KakaoApiClient,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
) {
    fun registerOrLogin(authCode: String): LoginResult {
        val tokenResponse = fetchKakaoUserAccessToken(authCode)
        val userInfo = kakaoApiClient.getUserInfo("Bearer ${tokenResponse.access_token}")
        val user = userRepository.findByKakaoId(userInfo.id.toString())
        if (user != null) {
            return LoginResult.normal(jwtProvider.generateAccessToken(user), jwtProvider.generateRefreshToken(user))
        }
        val newUser = User(kakaoId = userInfo.id.toString(), oauthProvider = OauthProvider.KAKAO)
        val accessToken = jwtProvider.generateAccessToken(newUser)
        newUser.refreshToken = jwtProvider.generateRefreshToken(newUser)
        userRepository.save(newUser)
        return LoginResult.newlyRegistered(accessToken, newUser.refreshToken!!)
    }

    private fun fetchKakaoUserAccessToken(authCode: String): KauthTokenResponse {
        return kakaoAuthClient.getToken(
            KauthTokenRequest(
                grant_type = "authorization_code",
                client_id = kakaoOauthProperties.restApiKey,
                redirect_uri = kakaoOauthProperties.callbackUrl,
                code = authCode,
                client_secret = kakaoOauthProperties.clientSecret,
            ),
        )
    }
}
