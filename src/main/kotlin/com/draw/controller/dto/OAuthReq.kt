package com.draw.controller.dto

import com.draw.common.enums.OAuthProvider

data class OAuthReq(
    val code: String,
    val provider: OAuthProvider,
)
