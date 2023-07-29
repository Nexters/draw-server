package com.draw.common

import com.draw.common.enums.ErrorType

class RefreshTokenExpiredException : BusinessException(ErrorType.REFRESH_TOKEN_EXPIRED)
