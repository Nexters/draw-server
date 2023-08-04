package com.draw.service

import com.draw.common.BusinessException
import com.draw.common.enums.ErrorType
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
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

    fun getFeed(inputUserId: Long?, feedId: Long): FeedRes {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        val isFavorite = inputUserId?.let { favoriteFeedRepository.existsByUserIdAndFeed(it, feed) } ?: false

        return FeedRes(
            id = feed.id!!,
            content = feed.content,
            isFavorite = isFavorite,
            favoriteCount = feed.favoriteCount,
            isFit = feed.isFit(userGender = Gender.MALE, 29, MBTI.ESTJ) // TODO: userAge 연동시 변경 2023/08/03 (koi)
        )
    }

    @Transactional
    fun createFeed(user: User, feedCreateReq: FeedCreateReq) {
        feedRepository.save(
            feedCreateReq.toEntity(user.id!!, user.getIntAge())
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

    fun getFeeds(userId: Long?, lastFeedId: Long?): FeedsRes {
        // TODO: userEntity 추가시 추가 개발 2023/08/04 (koi)
        val projections = userId?.let { feedRepository.findAllFeeds(it, 29) } ?: feedRepository.findAllFeeds()

        return FeedsRes(
            feeds = projections.map {
                FeedRes(
                    id = it.id,
                    content = it.content,
                    isFavorite = it.isFavorite,
                    favoriteCount = it.favoriteCount,
                    isFit = false // TODO: userEntity 추가시 개발 2023/08/04 (koi)
                )
            }.toList(),
            hasNext = false
        )
    }

    // TODO: lastFeedId 미고려 2023/08/03 (koi)
    fun getMyFeeds(user: User, lastFeedId: Long?): FeedsRes {
        val projections = feedRepository.findWriterFeeds(user.id!!)

        return FeedsRes(
            feeds = projections.map {
                FeedRes(
                    id = it.id,
                    content = it.content,
                    isFavorite = it.isFavorite,
                    favoriteCount = it.favoriteCount,
                    isFit = false // TODO: userEntity 추가시 개발 2023/08/04 (koi)
                )
            }.toList(),
            hasNext = false
        )
    }

    // TODO: lastFeedId 미고려 2023/08/03 (koi)
    fun getMyFavoriteFeeds(user: User, lastFeedId: Long?): FeedsRes {
        val projections = feedRepository.findUserFavoriteFeeds(user.id!!)

        return FeedsRes(
            feeds = projections.map {
                FeedRes(
                    id = it.id,
                    content = it.content,
                    isFavorite = it.isFavorite,
                    favoriteCount = it.favoriteCount,
                    isFit = false // TODO: userEntity 추가시 개발 2023/08/04 (koi)
                )
            }.toList(),
            hasNext = false
        )
    }
}
