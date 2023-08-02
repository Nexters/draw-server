package com.draw.infra.external.kakao

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "kakao-auth-api", url = "https://kauth.kakao.com")
@Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
interface KakaoAuthClient {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @PostMapping("/oauth/token")
    fun getToken(@SpringQueryMap parameters: Map<String, Any>): KauthTokenResponse
}

data class KauthTokenRequest(
    val grantType: String,
    val clientId: String,
    val redirectUri: String,
    val code: String,
    val clientSecret: String? = null,
) {
    fun toMap(): Map<String, Any> {
        return mapper.convertValue(this)
    }

    companion object {
        private val mapper = jacksonObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    }
}

data class KauthTokenResponse(
    val tokenType: String,
    val accessToken: String,
    val idToken: String?,
    val expiresIn: Int,
    val refreshToken: String,
    val refreshTokenExpiresIn: Int,
    val scope: String?,
)
