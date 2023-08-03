package com.draw.infra.persistence

import com.draw.domain.feed.QFavoriteFeed
import com.draw.domain.feed.QFeed
import com.draw.service.dto.FeedDtoProjection
import com.draw.service.dto.QFeedDtoProjection
import com.querydsl.jpa.impl.JPAQueryFactory

class FeedRepositorySupportImpl(
    private val queryFactory: JPAQueryFactory,
) : FeedRepositorySupport {

    override fun findAllFeedProjectionByWriterId(writerId: Long): List<FeedDtoProjection> {
        val favoriteFeed = QFavoriteFeed.favoriteFeed

        return queryFactory.select(
            QFeedDtoProjection(
                feed.id!!,
                feed.content,
                favoriteFeed.id.isNotNull,
                feed.favoriteCount,
                feed.genders,
                feed.ageRange,
                feed.mbtiChars,
                feed.createdAt,
            )
        )
            .from(feed)
            .leftJoin(favoriteFeed)
            .on(feed.eq(favoriteFeed.feed))
            .where(
                feed.writerId.eq(writerId),
            )
            .fetch()
    }

    companion object {
        private val feed = QFeed.feed
    }
}
