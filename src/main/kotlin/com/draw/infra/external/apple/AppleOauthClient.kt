package com.draw.infra.external.apple

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "apple-oauth-api", url = "https://appleid.apple.com")
interface AppleOauthClient {
    @GetMapping("/auth/keys")
    fun getApplePubKeys(): ApplePubKeyResponse
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
