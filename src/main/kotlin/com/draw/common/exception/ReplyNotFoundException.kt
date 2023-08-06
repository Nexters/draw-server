package com.draw.common.exception

import com.draw.common.BusinessException
import com.draw.common.enums.ErrorType

class ReplyNotFoundException : BusinessException(ErrorType.REPLY_NOT_FOUND)
