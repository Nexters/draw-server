package com.draw.common

import com.draw.common.enums.ErrorType

class BusinessException(
    val errorType: ErrorType
) : RuntimeException(errorType.message)
