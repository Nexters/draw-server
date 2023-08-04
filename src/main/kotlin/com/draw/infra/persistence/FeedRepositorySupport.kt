package com.draw.infra.persistence

import com.draw.service.dto.FeedProjection

interface FeedRepositorySupport {

    fun findFeedProjection(id: Long): FeedProjection?
    fun findFeedProjection(id: Long, userId: Long): FeedProjection?
    fun findAllFeedProjections(): List<FeedProjection>
    fun findAllFeedProjections(userId: Long, userAge: Int): List<FeedProjection>
    fun findWriterFeedProjections(writerId: Long): List<FeedProjection>
    fun findUserFavoriteFeedProjections(userId: Long): List<FeedProjection>
}
