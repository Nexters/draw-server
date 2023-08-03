package com.draw.infra.persistence

import com.draw.service.dto.FeedDtoProjection

interface FeedRepositorySupport {

    fun findAllFeeds(): List<FeedDtoProjection>
    // TODO: user로 변경 가능 2023/08/04 (koi)
    fun findAllFeeds(userId: Long, userIntAge: Int): List<FeedDtoProjection>

    fun findWriterFeeds(writerId: Long): List<FeedDtoProjection>
    fun findUserFavoriteFeeds(userId: Long): List<FeedDtoProjection>
}
