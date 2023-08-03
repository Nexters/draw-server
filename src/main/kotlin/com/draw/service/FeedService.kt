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
    fun createFeed(userId: Long, feedCreateReq: FeedCreateReq) {

        // TODO: userAge반환 로직으로 변경 2023/08/03 (koi)
        feedRepository.save(
            feedCreateReq.toEntity(userId, 29)
        )
    }

    @Transactional
    fun createFeedView(userId: Long, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        feed.addFeedViewHistory(userId)
    }

    @Transactional
    fun blockFeed(userId: Long, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        require(feed.writerId != userId) { "Not allowed block own feed" }

        feed.addBlockFeed(userId)
    }

    @Transactional
    fun claimFeed(userId: Long, feedId: Long) {
        blockFeed(userId, feedId)

        // TODO: claim 적재 로직 추가 2023/08/02 (koi)
    }

    @Transactional
    fun createFavoriteFeed(userId: Long, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()

        try {
            favoriteFeedRepository.save(
                FavoriteFeed(
                    userId = userId,
                    feed = feed,
                )
            )
        } catch (e: DataIntegrityViolationException) {
            throw BusinessException(ErrorType.FAVORITE_FEED_ALREADY_EXISTS, e)
        }
    }

    @Transactional
    fun deleteFavoriteFeed(userId: Long, feedId: Long) {
        favoriteFeedRepository.deleteByUserIdAndFeedId(userId, feedId)
    }

    // TODO: lastFeedId 미고려 2023/08/03 (koi)
    fun getMyFeeds(userId: Long, lastFeedId: Long?): FeedsRes {
        val feedDtoProjections =
            feedRepository.findAllFeedProjectionByWriterId(userId)

        return FeedsRes(
            feeds = feedDtoProjections
                .sortedByDescending { it.createdAt }
                .map {
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
