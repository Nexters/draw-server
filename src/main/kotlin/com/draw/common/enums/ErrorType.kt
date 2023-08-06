package com.draw.common.enums

enum class ErrorType(
    val code: Int,
    val message: String,
) {
    BAD_REQUEST(40000, "Bad request"),
    VALIDATION_FAILED(40001, "validation failed"),
    FAVORITE_FEED_ALREADY_EXISTS(40002, "Favorite feed already exists"),
    FEED_NOT_FOUND(40003, "Feed not found"),
    REPLY_NOT_FOUND(40004, "Reply not found"),

    UNKNOWN_ERROR(50000, "Unknown error"),

    // 에러코드들을 5자리로 하면 Http status 코드랑 조금 더 직관적으로 매칭될 것 같다
    UNAUTHORIZED(40100, "unauthorized"),
    ACCESS_TOKEN_EXPIRED(40110, "access token is expired"),
    REFRESH_TOKEN_EXPIRED(40111, "refresh token is expired"),
    FORBIDDEN(40300, "forbidden"),
    INCOMPLETE_REGISTRATION(40301, "registration is incomplete"),
}
