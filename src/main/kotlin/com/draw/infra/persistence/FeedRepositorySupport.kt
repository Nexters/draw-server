package com.draw.infra.persistence

import com.draw.service.dto.FeedDtoProjection

interface FeedRepositorySupport {

    fun findAllFeedProjectionByWriterId(writerId: Long): List<FeedDtoProjection>
}
