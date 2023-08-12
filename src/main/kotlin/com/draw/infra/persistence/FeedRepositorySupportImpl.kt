package com.draw.infra.persistence

import com.draw.common.enums.VisibleTarget
import com.draw.domain.feed.QBlockFeed
import com.draw.domain.feed.QFavoriteFeed
import com.draw.domain.feed.QFeed
import com.draw.domain.feed.QFeedViewHistory
import com.draw.domain.user.User
import com.draw.service.dto.FeedProjection
import com.draw.service.dto.QFeedProjection
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl

class FeedRepositorySupportImpl(
    private val queryFactory: JPAQueryFactory,
) : FeedRepositorySupport {
    override fun findFeedProjection(id: Long): FeedProjection? {

        return queryFactory.select(
            feedProjection()
        ).from(feed)
            .where(
                feed.id.eq(id)
            )
            .fetchOne()
    }

    override fun findFeedProjection(id: Long, userId: Long): FeedProjection? {
        val favoriteFeed = QFavoriteFeed.favoriteFeed

        return queryFactory.select(
            feedProjection(favoriteFeed)
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

    override fun findAllFeedProjections(lastFeedId: Long?): Slice<FeedProjection> {
        val results = queryFactory.select(
            feedProjection()
        ).from(feed)
            .where(
                ltFeedId(lastFeedId)
            )
            .orderBy(feed.id.desc())
            .limit((FEED_PAGE_SIZE + 1).toLong())
            .fetch()

        return resultsToSlice(results)
    }

    override fun findAllFeedProjections(user: User, lastFeedId: Long?): Slice<FeedProjection> {
        val favoriteFeed = QFavoriteFeed.favoriteFeed
        val blockFeed = QBlockFeed.blockFeed
        val feedViewHistory = QFeedViewHistory.feedViewHistory

        val userId = user.id!!
        val userAge = user.getAge()

        val results = queryFactory.select(
            feedProjection(favoriteFeed)
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
                ltFeedId(lastFeedId),
                blockFeed.id.isNull,
                feedViewHistory.id.isNull,
                feed.writerId.ne(userId),
                feed.visibleTarget.eq(VisibleTarget.of(userAge))
            )
            .orderBy(feed.id.desc())
            .limit((FEED_PAGE_SIZE + 1).toLong())
            .fetch()

        return resultsToSlice(results)
    }

    override fun findWriterFeedProjections(writerId: Long, lastFeedId: Long?): Slice<FeedProjection> {
        val favoriteFeed = QFavoriteFeed.favoriteFeed

        val results = queryFactory.select(
            feedProjection(favoriteFeed)
        )
            .from(feed)
            .leftJoin(favoriteFeed)
            .on(
                feed.eq(favoriteFeed.feed)
                    .and(favoriteFeed.userId.eq(writerId))
            )
            .where(
                ltFeedId(lastFeedId),
                feed.writerId.eq(writerId),
            )
            .orderBy(feed.id.desc())
            .limit((FEED_PAGE_SIZE + 1).toLong())
            .fetch()

        return resultsToSlice(results)
    }

    override fun findUserFavoriteFeedProjections(userId: Long, lastFavoriteId: Long?): Slice<FeedProjection> {
        val favoriteFeed = QFavoriteFeed.favoriteFeed

        val results = queryFactory.select(
            feedProjection(favoriteFeed)
        ).from(feed)
            .leftJoin(favoriteFeed)
            .on(
                feed.eq(favoriteFeed.feed)
                    .and(favoriteFeed.userId.eq(userId))
            )
            .where(
                lastFavoriteId?.let { favoriteFeed.id.lt(it) },
                favoriteFeed.id.isNotNull,
                favoriteFeed.userId.eq(userId),
            )
            .orderBy(favoriteFeed.id.desc())
            .limit((FEED_PAGE_SIZE + 1).toLong())
            .fetch()

        return resultsToSlice(results)
    }

    private fun ltFeedId(lastFeedId: Long?) = lastFeedId?.let { feed.id.lt(lastFeedId) }

    private fun feedProjection(favoriteFeed: QFavoriteFeed? = null) = QFeedProjection(
        feed.id!!,
        feed.content,
        favoriteFeed?.id ?: Expressions.nullExpression(),
        feed.favoriteCount,
        feed.genders,
        feed.ageRange,
        feed.mbtiChars,
        feed.createdAt,
    )

    private fun resultsToSlice(results: MutableList<FeedProjection>): SliceImpl<FeedProjection> {
        if (results.size > FEED_PAGE_SIZE) {
            return SliceImpl(results.subList(0, FEED_PAGE_SIZE), Pageable.unpaged(), true)
        }
        return SliceImpl(results, Pageable.unpaged(), false)
    }

    companion object {
        private val feed = QFeed.feed
        private const val FEED_PAGE_SIZE = 20
    }
}
