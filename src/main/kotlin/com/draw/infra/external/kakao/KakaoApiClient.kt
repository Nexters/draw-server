package com.draw.infra.external.kakao

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "kakao-api", url = "https://kapi.kakao.com")
@Headers(
    "Content-Type: application/x-www-form-urlencoded;charset=utf-8",
)
interface KakaoApiClient {

    @GetMapping("/v2/user/me")
    fun getUserInfo(@RequestHeader("Authorization") token: String): UserInfoResponse

    @PostMapping("/v1/user/unlink")
    fun unlink(
        @RequestHeader("Authorization") token: String,
        @SpringQueryMap parameters: Map<String, Any>,
    ): KakaoUnlinkResponse
}

// 참고: https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response
data class UserInfoResponse(
    val id: Long,
    val connectedAt: String?,
    val kakaoAccount: Any?,
)

data class KakaoUnlinkRequest(
    val targetIdType: String,
    val targetId: Long,
) {
    fun toMap(): Map<String, Any> {
        return mapper.convertValue(this)
    }

    companion object {
        private val mapper = jacksonObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    }
}

data class KakaoUnlinkResponse(
    val id: Long,
)
