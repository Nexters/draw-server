package com.draw.infra.persistence

import com.draw.service.dto.FeedDtoProjection

interface FeedRepositorySupport {

    fun findWriterFeeds(writerId: Long): List<FeedDtoProjection>

    fun findUserFavoriteFeeds(userId: Long): List<FeedDtoProjection>
}
