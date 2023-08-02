package com.draw.service

import com.draw.common.BusinessException
import com.draw.common.enums.ErrorType
import com.draw.controller.dto.FeedCreateReq
import com.draw.controller.dto.FeedDetailRes
import com.draw.controller.dto.ReplyRes
import com.draw.controller.dto.ReplyStatus
import com.draw.domain.feed.FavoriteFeed
import com.draw.domain.reply.Reply
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

    fun getFeed(userId: Long?, feedId: Long): FeedDetailRes {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw IllegalArgumentException("존재하지 않는 피드입니다.")
        val isFavorite = userId?.let { favoriteFeedRepository.existsByUserIdAndFeed(it, feed) } ?: false

        // TODO: blocked filtering 로직 추가 2023/08/02 (koi)
        return FeedDetailRes(
            id = feed.id!!,
            content = feed.content,
            isFavorite = isFavorite,
            favoriteCount = feed.favoriteCount,
            isFit = false,
            replies = feed.replies
                .map { reply ->
                    ReplyRes(
                        id = reply.id!!,
                        content = reply.content,
                        status = reply.getStatus(userId), // TODO: 수정 필요 2023/08/02 (koi)
                        writerId = reply.writerId,
                        writer = null // TODO: 개발 필요 2023/08/02 (koi)
                    )
                }.toList(),
        )
    }

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
    fun blockFeed(userId: Long, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw IllegalArgumentException("존재하지 않는 피드입니다.")
        feed.addBlockFeed(userId)
    }

    @Transactional
    fun claimFeed(userId: Long, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw IllegalArgumentException("존재하지 않는 피드입니다.")
        feed.addBlockFeed(userId)

        // TODO: claim 적재 로직 추가 2023/08/02 (koi)
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

    private fun Reply.getStatus(userId: Long?): ReplyStatus {
        if (userId == null) {
            return ReplyStatus.NORMAL
        }

        if (writerId == userId) {
            return ReplyStatus.MINE
        }

        // TODO: peeked 2023/08/02 (koi)
        return ReplyStatus.NORMAL
    }
}
