package com.draw.infra.persistence

import com.draw.common.enums.VisibleTarget
import com.draw.domain.feed.QBlockFeed
import com.draw.domain.feed.QFavoriteFeed
import com.draw.domain.feed.QFeed
import com.draw.domain.feed.QFeedViewHistory
import com.draw.service.dto.FeedDtoProjection
import com.draw.service.dto.QFeedDtoProjection
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory

class FeedRepositorySupportImpl(
    private val queryFactory: JPAQueryFactory,
) : FeedRepositorySupport {

    override fun findAllFeeds(): List<FeedDtoProjection> {

        return queryFactory.select(
            QFeedDtoProjection(
                feed.id!!,
                feed.content,
                Expressions.asBoolean(false),
                feed.favoriteCount,
                feed.genders,
                feed.ageRange,
                feed.mbtiChars,
                feed.createdAt,
            )
        ).from(feed)
            .fetch()
            .sortedByDescending { it.createdAt }
    }

    override fun findAllFeeds(userId: Long, userIntAge: Int): List<FeedDtoProjection> {
        val favoriteFeed = QFavoriteFeed.favoriteFeed
        val blockFeed = QBlockFeed.blockFeed
        val feedViewHistory = QFeedViewHistory.feedViewHistory

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
        ).from(feed)
            .leftJoin(favoriteFeed)
            .on(
                feed.eq(favoriteFeed.feed)
                    .and(favoriteFeed.userId.eq(userId))
            )
            .leftJoin(blockFeed)
            .on(
                feed.eq(blockFeed.feed)
                    .and(blockFeed.userId.eq(userId))
            )
            .leftJoin(feedViewHistory)
            .on(
                feed.eq(feedViewHistory.feed)
                    .and(feedViewHistory.userId.eq(userId))
            )
            .where(
                blockFeed.id.isNull,
                feedViewHistory.id.isNull,
                feed.writerId.ne(userId),
                feed.visibleTarget.eq(VisibleTarget.of(userIntAge))
            )
            .fetch()
            .sortedByDescending { it.createdAt }
    }

    override fun findWriterFeeds(writerId: Long): List<FeedDtoProjection> {
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
            .sortedByDescending { it.createdAt }
    }

    override fun findUserFavoriteFeeds(userId: Long): List<FeedDtoProjection> {
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
        ).from(feed)
            .leftJoin(favoriteFeed)
            .on(feed.eq(favoriteFeed.feed))
            .where(
                favoriteFeed.id.isNotNull,
                favoriteFeed.userId.eq(userId),
            )
            .fetch()
            .sortedByDescending { it.createdAt }
    }

    companion object {
        private val feed = QFeed.feed
    }
}
