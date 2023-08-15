package com.draw.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("kakao.auth")
data class KakaoOAuthProperties(
    val adminKey: String,
    val clientSecret: String,
    val restApiKey: String,
    val jsKey: String,
    val callbackUrl: String,
)
