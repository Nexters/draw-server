package com.draw.infra.persistence

import com.draw.common.enums.VisibleTarget
import com.draw.domain.feed.QBlockFeed
import com.draw.domain.feed.QFavoriteFeed
import com.draw.domain.feed.QFeed
import com.draw.domain.feed.QFeedViewHistory
import com.draw.service.dto.FeedProjection
import com.draw.service.dto.QFeedProjection
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory

class FeedRepositorySupportImpl(
    private val queryFactory: JPAQueryFactory,
) : FeedRepositorySupport {
    override fun findFeedProjection(id: Long): FeedProjection? {

        return queryFactory.select(
            feedDtoProjection()
        ).from(feed)
            .where(
                feed.id.eq(id)
            )
            .fetchOne()
    }

    override fun findFeedProjection(id: Long, userId: Long): FeedProjection? {
        val favoriteFeed = QFavoriteFeed.favoriteFeed

        return queryFactory.select(
            feedDtoProjection(favoriteFeed)
        ).from(feed)
            .leftJoin(favoriteFeed)
            .on(
                feed.eq(favoriteFeed.feed)
                    .and(favoriteFeed.userId.eq(userId))
            ).where(
                feed.id.eq(id)
            )
            .fetchOne()
    }

    override fun findAllFeedProjections(): List<FeedProjection> {

        return queryFactory.select(
            feedDtoProjection()
        ).from(feed)
            .fetch()
            .sortedByDescending { it.createdAt }
    }

    override fun findAllFeedProjections(userId: Long, userAge: Int): List<FeedProjection> {
        val favoriteFeed = QFavoriteFeed.favoriteFeed
        val blockFeed = QBlockFeed.blockFeed
        val feedViewHistory = QFeedViewHistory.feedViewHistory

        return queryFactory.select(
            feedDtoProjection(favoriteFeed)
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
                feed.visibleTarget.eq(VisibleTarget.of(userAge))
            )
            .fetch()
            .sortedByDescending { it.createdAt }
    }

    override fun findWriterFeedProjections(writerId: Long): List<FeedProjection> {
        val favoriteFeed = QFavoriteFeed.favoriteFeed

        return queryFactory.select(
            feedDtoProjection(favoriteFeed)
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

    override fun findUserFavoriteFeedProjections(userId: Long): List<FeedProjection> {
        val favoriteFeed = QFavoriteFeed.favoriteFeed

        return queryFactory.select(
            feedDtoProjection(favoriteFeed)
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

    private fun feedDtoProjection(favoriteFeed: QFavoriteFeed? = null) = QFeedProjection(
        feed.id!!,
        feed.content,
        favoriteFeed?.let { it.id!!.isNotNull } ?: Expressions.asBoolean(false),
        feed.favoriteCount,
        feed.genders,
        feed.ageRange,
        feed.mbtiChars,
        feed.createdAt,
    )

    companion object {
        private val feed = QFeed.feed
    }
}
