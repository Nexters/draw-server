package com.draw.service

import com.draw.common.BusinessException
import com.draw.common.enums.ErrorType
import com.draw.controller.dto.FeedCreateReq
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

    @Transactional
    fun createFeed(userId: Long, feedCreateReq: FeedCreateReq) {
        feedRepository.save(
            feedCreateReq.toEntity(userId)
        )
    }

    @Transactional
    fun createFeedView(userId: Long, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw IllegalArgumentException("존재하지 않는 피드입니다.")
        feed.addFeedViewHistory(userId)
    }

    @Transactional
    fun createFavoriteFeed(userId: Long, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw IllegalArgumentException("존재하지 않는 피드입니다.")

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
}
