package com.draw.infra.persistence

import com.draw.domain.user.User
import com.draw.service.dto.FeedProjection
import org.springframework.data.domain.Slice

interface FeedRepositorySupport {

    fun findFeedProjection(id: Long): FeedProjection?
    fun findFeedProjection(id: Long, userId: Long): FeedProjection?
    fun findAllFeedProjections(lastFeedId: Long?): Slice<FeedProjection>
    fun findAllFeedProjections(user: User, lastFeedId: Long?): Slice<FeedProjection>
    fun findWriterFeedProjections(writerId: Long, lastFeedId: Long?): Slice<FeedProjection>
    fun findUserFavoriteFeedProjections(userId: Long, lastFeedId: Long?): Slice<FeedProjection>
}
