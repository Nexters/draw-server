package com.draw.infra.external.kakao

import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "kakao-api", url = "https://kapi.kakao.com")
@Headers(
    "Content-Type: application/x-www-form-urlencoded;charset=utf-8",
)
interface KakaoApiClient {

    @GetMapping("/v2/user/me")
    fun getUserInfo(@RequestHeader("Authorization") token: String): UserInfoResponse
}

// 참고: https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response
data class UserInfoResponse(
    val id: Long,
    val connectedAt: Long?,
)
