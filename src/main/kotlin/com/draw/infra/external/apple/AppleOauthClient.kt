package com.draw.infra.external.apple

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "apple-oauth-api", url = "https://appleid.apple.com")
interface AppleOauthClient {
    @GetMapping("/auth/keys")
    fun getApplePubKeys(): ApplePubKeyResponse

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @PostMapping("/auth/token")
    fun validateToken(
        @SpringQueryMap parameters: Map<String, Any>,
    ): TokenResponse

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @PostMapping("/auth/revoke")
    fun revoke(
        @SpringQueryMap parameters: Map<String, Any>,
    )
}

data class ApplePubKey(
    val alg: String,
    val e: String,
    val kid: String,
    val kty: String,
    val n: String,
    val use: String,
)

data class ApplePubKeyResponse(
    val keys: List<ApplePubKey>,
)

data class TokenResponse(
    val accessToken: String,
    val expiresIn: Long,
    val idToken: String,
    val refreshToken: String,
    val tokenType: String,
)

data class TokenRequest(
    val clientId: String,
    val clientSecret: String,
    val code: String?,
    val grantType: String,
    val refreshToken: String?,
    val redirectUri: String? = null,
) {
    fun toMap(): Map<String, Any> {
        return mapper.convertValue(this)
    }

    companion object {
        private val mapper = jacksonObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    }
}

data class RevokeRequest(
    val clientId: String,
    val clientSecret: String,
    val token: String,
    val tokenTypeHint: String,
) {
    fun toMap(): Map<String, Any> {
        return mapper.convertValue(this)
    }

    companion object {
        private val mapper = jacksonObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    }
}
