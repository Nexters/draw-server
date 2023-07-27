package com.draw.infra.persistence

import com.draw.domain.feed.FavoriteFeed
import org.springframework.data.jpa.repository.JpaRepository

interface FavoriteFeedRepository : JpaRepository<FavoriteFeed, Long> {
    fun findByUserIdAndFeedId(userId: Long, feedId: Long): FavoriteFeed?
    fun deleteByUserIdAndFeedId(userId: Long, feedId: Long)
    fun findByUserId(userId: Long): List<FavoriteFeed>
}
