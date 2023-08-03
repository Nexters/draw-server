package com.draw.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auth.jwt")
data class JwtProperties(
    val secretKey: String,
    val accessTokenExpireMs: Long,
    val refreshTokenExpireMs: Long,
)
