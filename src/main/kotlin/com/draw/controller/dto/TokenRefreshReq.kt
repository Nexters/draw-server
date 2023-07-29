package com.draw.controller.dto

data class TokenRefreshReq(
    val accessToken: String,
    val refreshToken: String,
)
