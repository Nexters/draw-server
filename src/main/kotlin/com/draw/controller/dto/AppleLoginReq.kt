package com.draw.controller.dto

data class AppleLoginReq(
    val code: String,
    val idToken: String,
    val state: String?,
    val user: String,
    val error: String?,
)
