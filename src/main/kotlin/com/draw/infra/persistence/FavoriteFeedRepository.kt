package com.draw.infra.persistence

import com.draw.domain.feed.FavoriteFeed
import com.draw.domain.feed.Feed
import org.springframework.data.jpa.repository.JpaRepository

interface FavoriteFeedRepository : JpaRepository<FavoriteFeed, Long> {
    fun existsByUserIdAndFeed(userId: Long, feed: Feed): Boolean
    fun deleteByUserIdAndFeedId(userId: Long, feedId: Long)
}
