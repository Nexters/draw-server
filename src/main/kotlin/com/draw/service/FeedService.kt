package com.draw.service

import com.draw.domain.feed.FavoriteFeed
import com.draw.infra.persistence.FavoriteFeedRepository
import com.draw.infra.persistence.FeedRepository
import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FeedService(
    private val feedRepository: FeedRepository,
    private val favoriteFeedRepository: FavoriteFeedRepository,
) {
    private val log = KotlinLogging.logger { }

    fun createFavoriteFeed(userId: Long, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw IllegalArgumentException("존재하지 않는 피드입니다.")

        try {
            favoriteFeedRepository.save(
                FavoriteFeed(
                    userId = userId,
                    feed = feed,
                )
            )
        } catch (ignore: DataIntegrityViolationException) {
            log.info { "${ignore.message}" }
        }
    }

    fun deleteFavoriteFeed(userId: Long, feedId: Long) {
        favoriteFeedRepository.deleteByUserIdAndFeedId(userId, feedId)
    }
}
