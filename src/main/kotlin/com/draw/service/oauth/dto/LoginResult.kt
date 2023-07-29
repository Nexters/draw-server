package com.draw.service.oauth.dto

data class LoginResult(
    val result: Status,
    val accessToken: String? = null,
    val refreshToken: String? = null,
) {
    enum class Status {
        NEWLY_REGISTERED,
        NORMAL, // 정상적인 유저
        NOT_FOUND, // 유저가 존재하지 않는 경우
        SUSPENDED, // 제제된 경우
    }

    companion object {
        fun newlyRegistered(accessToken: String, refreshToken: String) = LoginResult(Status.NEWLY_REGISTERED, accessToken, refreshToken)
        fun normal(accessToken: String, refreshToken: String) = LoginResult(Status.NORMAL, accessToken, refreshToken)
        fun notFound() = LoginResult(Status.NOT_FOUND)
    }
}
