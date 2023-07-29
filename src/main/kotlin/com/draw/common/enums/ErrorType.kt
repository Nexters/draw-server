package com.draw.common.enums

enum class ErrorType(
    val code: Int,
    val message: String,
) {

    BAD_REQUEST(4000, "Bad request"),
    VALIDATION_FAILED(4001, "validation failed"),

    UNKNOWN_ERROR(5000, "Unknown error"),

    // 에러코드들을 5자리로 하면 Http status 코드랑 조금 더 직관적으로 매칭될 것 같다
    UNAUTHORIZED(4010, "unauthorized"),
    ACCESS_TOKEN_EXPIRED(4011, "access token is expired"),
    REFRESH_TOKEN_EXPIRED(4012, "refresh token is expired"),
    FORBIDDEN(4300, "forbidden"),
    INCOMPLETE_REGISTRATION(4301, "registration is incomplete"),
}
