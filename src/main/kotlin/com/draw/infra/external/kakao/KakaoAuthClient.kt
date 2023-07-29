package com.draw.infra.external.kakao

import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "kakao-auth-api", url = "https://kauth.kakao.com")
@Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
interface KakaoAuthClient {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @PostMapping("/oauth/token")
    fun getToken(@SpringQueryMap parameters: KauthTokenRequest): KauthTokenResponse
}

data class KauthTokenRequest(
    val grant_type: String,
    val client_id: String,
    val redirect_uri: String,
    val code: String,
    val client_secret: String? = null,
)

data class KauthTokenResponse(
    val token_type: String,
    val access_token: String,
    val id_token: String?,
    val expires_in: Int,
    val refresh_token: String,
    val refresh_token_expires_in: Int,
    val scope: String?,
)
