package com.draw.common

import com.draw.common.enums.ErrorType

class BusinessException(
    val errorType: ErrorType,
    cause: Exception? = null,
) : RuntimeException(errorType.message, cause)
