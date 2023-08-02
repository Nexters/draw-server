package com.draw.common.enums

enum class ErrorType(
    val code: Int,
    val message: String,
) {

    BAD_REQUEST(4000, "Bad request"),
    VALIDATION_FAILED(4001, "validation failed"),
    FAVORITE_FEED_ALREADY_EXISTS(4002, "Favorite feed already exists"),
    FEED_NOT_FOUND(4003, "Feed not found"),
    REPLY_NOT_FOUND(4004, "Reply not found"),

    UNKNOWN_ERROR(5000, "Unknown error")
}
