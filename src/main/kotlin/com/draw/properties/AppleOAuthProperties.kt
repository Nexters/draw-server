package com.draw.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("apple.auth")
data class AppleOAuthProperties(
    val serviceId: String,
    val teamId: String,
    val bundleId: String,
    val aud: String,
    val privateKey: String,
    val kid: String,
)
