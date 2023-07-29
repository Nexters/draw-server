package com.draw.service.oauth.dto

data class TokenRefreshResult(
    val accessToken: String,
    val refreshToken: String,
)
