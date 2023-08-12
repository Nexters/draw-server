package com.draw.common.exception

import com.draw.common.BusinessException
import com.draw.common.enums.ErrorType

class UserNotFoundException : BusinessException(ErrorType.USER_NOT_FOUND)
