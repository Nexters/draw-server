package com.draw.service

import com.draw.common.BusinessException
import com.draw.common.enums.ErrorType
import com.draw.common.exception.FeedNotFoundException
import com.draw.controller.dto.FeedCreateReq
import com.draw.controller.dto.FeedRes
import com.draw.controller.dto.FeedsRes
import com.draw.domain.feed.FavoriteFeed
import com.draw.domain.user.User
import com.draw.infra.persistence.FavoriteFeedRepository
import com.draw.infra.persistence.FeedRepository
import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FeedService(
    private val feedRepository: FeedRepository,
    private val favoriteFeedRepository: FavoriteFeedRepository,
) {
    private val log = KotlinLogging.logger { }

    fun getFeed(user: User?, feedId: Long): FeedRes {
        val feedProjection = user?.let { feedRepository.findFeedProjection(feedId, it.id!!) }
            ?: feedRepository.findFeedProjection(feedId)
            ?: throw FeedNotFoundException()

        return FeedRes(
            id = feedProjection.id,
            content = feedProjection.content,
            isFavorite = feedProjection.isFavorite,
            favoriteCount = feedProjection.favoriteCount,
            isFit = user?.let { feedProjection.isFit(it.gender, it.getAge(), it.mbti) } ?: false
        )
    }

    @Transactional
    fun createFeed(user: User, feedCreateReq: FeedCreateReq) {
        feedRepository.save(
            feedCreateReq.toEntity(user.id!!, user.getAge())
        )
    }

    @Transactional
    fun createFeedView(user: User, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        feed.addFeedViewHistory(user.id!!)
    }

    @Transactional
    fun blockFeed(user: User, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        require(feed.writerId != user.id!!) { "Not allowed block own feed" }

        feed.addBlockFeed(user.id!!)
    }

    @Transactional
    fun claimFeed(user: User, feedId: Long) {
        blockFeed(user, feedId)

        // TODO: 신고하기 로직 추가 2023/08/04 (koi)
    }

    @Transactional
    fun createFavoriteFeed(user: User, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()

        try {
            favoriteFeedRepository.save(
                FavoriteFeed(
                    userId = user.id!!,
                    feed = feed,
                )
            )
        } catch (e: DataIntegrityViolationException) {
            throw BusinessException(ErrorType.FAVORITE_FEED_ALREADY_EXISTS, e)
        }
    }

    @Transactional
    fun deleteFavoriteFeed(user: User, feedId: Long) {
        favoriteFeedRepository.deleteByUserIdAndFeedId(user.id!!, feedId)
    }

    fun getFeeds(user: User?, lastFeedId: Long?): FeedsRes {
        val slice =
            user?.let { feedRepository.findAllFeedProjections(it, lastFeedId) }
                ?: feedRepository.findAllFeedProjections(lastFeedId)

        return FeedsRes(
            feeds = slice.content.map { feedProjection ->
                FeedRes(
                    id = feedProjection.id,
                    content = feedProjection.content,
                    isFavorite = feedProjection.isFavorite,
                    favoriteCount = feedProjection.favoriteCount,
                    isFit = user?.let { feedProjection.isFit(it.gender, it.getAge(), it.mbti) } ?: false
                )
            }.toList(),
            hasNext = slice.hasNext()
        )
    }

    fun getMyFeeds(user: User, lastFeedId: Long?): FeedsRes {
        val slice = feedRepository.findWriterFeedProjections(user.id!!, lastFeedId)

        return FeedsRes(
            feeds = slice.content.map { feedProjection ->
                FeedRes(
                    id = feedProjection.id,
                    content = feedProjection.content,
                    isFavorite = feedProjection.isFavorite,
                    favoriteCount = feedProjection.favoriteCount,
                    isFit = feedProjection.isFit(user.gender, user.getAge(), user.mbti)
                )
            }.toList(),
            hasNext = slice.hasNext()
        )
    }

    fun getMyFavoriteFeeds(user: User, lastFeedId: Long?): FeedsRes {
        val slice = feedRepository.findUserFavoriteFeedProjections(user.id!!, lastFeedId)

        return FeedsRes(
            feeds = slice.content.map { feedProjection ->
                FeedRes(
                    id = feedProjection.id,
                    content = feedProjection.content,
                    isFavorite = feedProjection.isFavorite,
                    favoriteCount = feedProjection.favoriteCount,
                    isFit = feedProjection.isFit(user.gender, user.getAge(), user.mbti)
                )
            }.toList(),
            hasNext = slice.hasNext()
        )
    }
}
