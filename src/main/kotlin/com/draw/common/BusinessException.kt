package com.draw.common

import com.draw.common.enums.ErrorType

open class BusinessException(
    val errorType: ErrorType
) : RuntimeException(errorType.message)
