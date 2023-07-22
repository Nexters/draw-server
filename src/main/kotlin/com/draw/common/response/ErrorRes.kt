package com.draw.common.response

import com.draw.common.enums.ErrorType

data class ErrorRes(
    val code: Int,
    val message: String,
) {
    companion object {
        fun of(errorType: ErrorType): ErrorRes {
            return ErrorRes(
                code = errorType.code,
                message = errorType.message
            )
        }

        fun of(errorType: ErrorType, message: String): ErrorRes {
            return ErrorRes(
                code = errorType.code,
                message = "$errorType.message ($message)"
            )
        }
    }
}
