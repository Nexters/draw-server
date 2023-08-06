package com.draw.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("apple.auth")
data class AppleOAuthProperties(
    val teamId: String,
    val aud: String,
)
