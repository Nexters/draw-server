package com.draw.common.exception

import com.draw.common.BusinessException
import com.draw.common.enums.ErrorType

class FeedNotFoundException : BusinessException(ErrorType.FEED_NOT_FOUND)
