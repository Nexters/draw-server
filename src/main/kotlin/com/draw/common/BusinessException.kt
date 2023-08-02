package com.draw.common

import com.draw.common.enums.ErrorType

open class BusinessException(
    val errorType: ErrorType,
    cause: Exception? = null,
) : RuntimeException(errorType.message, cause)
