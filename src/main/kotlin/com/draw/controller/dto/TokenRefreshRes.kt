package com.draw.controller.dto

data class TokenRefreshRes(
    val accessToken: String,
    val refreshToken: String,
)
