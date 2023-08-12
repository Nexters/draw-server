package com.draw.common

import com.draw.common.enums.ErrorType

class AccessTokenExpiredException : BusinessException(ErrorType.ACCESS_TOKEN_EXPIRED)
