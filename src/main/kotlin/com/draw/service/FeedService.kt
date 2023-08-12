package com.draw.service

import com.draw.common.BusinessException
import com.draw.common.enums.ClaimOriginType
import com.draw.common.enums.ErrorType
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
import com.draw.common.exception.FeedNotFoundException
import com.draw.controller.dto.FeedCreateReq
import com.draw.controller.dto.FeedRes
import com.draw.controller.dto.FeedsRes
import com.draw.controller.dto.MyFavoriteFeedRes
import com.draw.controller.dto.MyFavoriteFeedsRes
import com.draw.domain.claim.Claim
import com.draw.domain.feed.FavoriteFeed
import com.draw.domain.feed.Feed
import com.draw.domain.user.User
import com.draw.infra.persistence.ClaimRepository
import com.draw.infra.persistence.FavoriteFeedRepository
import com.draw.infra.persistence.FeedRepository
import com.draw.service.dto.FeedProjection
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
    private val claimRepository: ClaimRepository,
) {
    private val log = KotlinLogging.logger { }

    fun getFeed(user: User?, feedId: Long): FeedRes {
        val feedProjection = user?.let { feedRepository.findFeedProjection(feedId, it.id!!) }
            ?: feedRepository.findFeedProjection(feedId)
            ?: throw FeedNotFoundException()

        return FeedRes(
            id = feedProjection.id,
            content = feedProjection.content,
            isFavorite = feedProjection.isFavorite(),
            favoriteCount = feedProjection.favoriteCount,

            isFit = user?.let { isFit(feedProjection, it) }
                ?: false,
        )
    }

    @Transactional
    fun createFeed(user: User, feedCreateReq: FeedCreateReq) {
        feedRepository.save(
            feedCreateReq.toEntity(user.id!!, user.getAge()),
        )
    }

    @Transactional
    fun createFeedView(user: User, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        feed.addFeedViewHistory(user.id!!)
    }

    @Transactional
    fun blockFeed(user: User, feedId: Long): Feed {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        require(feed.writerId != user.id!!) { "Not allowed block own feed" }

        feed.addBlockFeed(user.id!!)
        return feed
    }

    @Transactional
    fun claimFeed(user: User, feedId: Long) {
        val feed = blockFeed(user, feedId)
        claimRepository.save(
            Claim(
                reportedUserId = feed.writerId,
                informantUserId = user.id!!,
                originId = feedId,
                originType = ClaimOriginType.FEED,
            )
        )
    }

    @Transactional
    fun createFavoriteFeed(user: User, feedId: Long) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()

        try {
            favoriteFeedRepository.save(
                FavoriteFeed(
                    userId = user.id!!,
                    feed = feed,
                ),
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
                    isFavorite = feedProjection.isFavorite(),
                    favoriteCount = feedProjection.favoriteCount,
                    isFit = user?.let { isFit(feedProjection, it) } ?: false,
                )
            }.toList(),
            hasNext = slice.hasNext(),
        )
    }

    fun getMyFeeds(user: User, lastFeedId: Long?): FeedsRes {
        val slice = feedRepository.findWriterFeedProjections(user.id!!, lastFeedId)

        return FeedsRes(
            feeds = slice.content.map { feedProjection ->
                FeedRes(
                    id = feedProjection.id,
                    content = feedProjection.content,
                    isFavorite = feedProjection.isFavorite(),
                    favoriteCount = feedProjection.favoriteCount,
                    isFit = isFit(feedProjection, user),
                )
            }.toList(),
            hasNext = slice.hasNext(),
        )
    }

    fun getMyFavoriteFeeds(user: User, lastFavoriteId: Long?): MyFavoriteFeedsRes {
        val slice = feedRepository.findUserFavoriteFeedProjections(user.id!!, lastFavoriteId)

        return MyFavoriteFeedsRes(
            myFavoriteFeeds = slice.content.map { feedProjection ->
                MyFavoriteFeedRes(
                    id = feedProjection.id,
                    content = feedProjection.content,
                    isFavorite = feedProjection.isFavorite(),
                    favoriteCount = feedProjection.favoriteCount,
                    favoriteId = feedProjection.favoriteId!!,
                    isFit = isFit(feedProjection, user),
                )
            }.toList(),
            hasNext = slice.hasNext(),
        )
    }

    // larry.x 백도어 API 로 가입완료 시킨 유저들 대응하기 위해서 임시로 추가
    private fun isFit(feedProjection: FeedProjection, user: User): Boolean {
        return feedProjection.isFit(user.gender ?: Gender.MALE, user.getAge(), user.mbti ?: MBTI.ENFJ)
    }
}
