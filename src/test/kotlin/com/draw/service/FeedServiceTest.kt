package com.draw.service

import com.draw.common.BusinessException
import com.draw.common.enums.AgeOption
import com.draw.common.enums.AgeRange
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTIChar
import com.draw.common.enums.VisibleTarget
import com.draw.common.exception.FeedNotFoundException
import com.draw.controller.dto.FeedCreateReq
import com.draw.domain.feed.FavoriteFeed
import com.draw.domain.feed.Feed
import com.draw.domain.user.User
import com.draw.infra.persistence.ClaimRepository
import com.draw.infra.persistence.FavoriteFeedRepository
import com.draw.infra.persistence.FeedRepository
import com.draw.service.dto.FeedProjection
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import java.time.ZonedDateTime

@ExtendWith(value = [MockKExtension::class])
class FeedServiceTest {

    private val feedRepository = mockk<FeedRepository>()
    private val favoriteFeedRepository = mockk<FavoriteFeedRepository>(relaxUnitFun = true)
    private val claimRepository = mockk<ClaimRepository>(relaxUnitFun = true)

    private val feedService = FeedService(feedRepository, favoriteFeedRepository, claimRepository)

    private lateinit var feed: Feed
    private lateinit var user: User
    private lateinit var user2: User

    @BeforeEach
    fun setUp() {
        feed = Feed(content = "content", writerId = 1L, visibleTarget = VisibleTarget.ADULT)
            .apply { id = 1L }

        user = User(
            id = 1L,
        )

        user2 = User(
            id = 2L,
        )
    }

    @Test
    fun `내가 좋아요를 누른 피드면, isFavorite 값이 true로 반환된다`() {
        // given
        every { feedRepository.findFeedProjection(1L, user.id!!) } returns FeedProjection(
            id = 1L,
            content = "content",
            genders = mutableListOf(Gender.MALE, Gender.FEMALE),
            mbtiChars = mutableListOf(MBTIChar.E, MBTIChar.S, MBTIChar.T),
            ageRange = AgeRange.ALL,
            createdAt = ZonedDateTime.now(),
            favoriteId = 1L,
            favoriteCount = 10,
        )

        // when
        val res = feedService.getFeed(user, 1L)

        // then
        assertThat(res.isFavorite).isTrue
    }

    @Test
    fun `피드가 생성된다`() {
        // given
        every { feedRepository.save(any()) } returns feed
        val req =
            FeedCreateReq(content = "content", genders = listOf(), ageOption = AgeOption.ALL, mbtiChars = listOf())

        // when
        feedService.createFeed(user, req)

        // then
        verify { feedRepository.save(any()) }
    }

    @Test
    fun `피드 조회 히스토리가 추가된다`() {
        // given
        every { feedRepository.findByIdOrNull(1L) } returns feed

        // when
        feedService.createFeedView(user, 1L)

        // then
        assertThat(feed.feedViewHistories).hasSize(1)
    }

    @Test
    fun `피드 차단이 생성된다`() {
        // given
        every { feedRepository.findByIdOrNull(1L) } returns feed

        // when
        feedService.blockFeed(user2, 1L)

        // then
        assertThat(feed.blockFeeds).hasSize(1)
    }

    @Test
    fun `내 피드는 차단할 수 없다`() {
        // given
        every { feedRepository.findByIdOrNull(1L) } returns feed

        // when, then
        assertThrows<IllegalArgumentException> { feedService.blockFeed(user, 1L) }
    }

    @Test
    fun `피드 신고시에도 차단이 생성된다`() {
        // given
        every { feedRepository.findByIdOrNull(1L) } returns feed
        every { claimRepository.save(any()) } returns mockk()

        // when
        feedService.claimFeed(user2, 1L)

        // then
        assertThat(feed.blockFeeds).hasSize(1)
        verify(exactly = 1) { claimRepository.save(any()) }
    }

    @Test
    fun `내 피드는 신고할 수 없다`() {
        // given
        every { feedRepository.findByIdOrNull(1L) } returns feed

        // when, then
        assertThrows<IllegalArgumentException> { feedService.claimFeed(user, 1L) }
    }

    @Test
    fun `피드 좋아요가 생성된다`() {
        // given
        every { feedRepository.findByIdOrNull(1L) } returns feed
        every { favoriteFeedRepository.save(any()) } returns FavoriteFeed(
            userId = 1L,
            feed = feed
        )

        // when
        feedService.createFavoriteFeed(user, 1L)

        // then
        verify { favoriteFeedRepository.save(any()) }
    }

    @Test
    fun `한 피드에 중복 좋아요가 유입되면, 예외가 발생하며, 4002 에러코드를 반환한다`() {
        // given
        every { feedRepository.findByIdOrNull(1L) } returns feed
        every { favoriteFeedRepository.save(any()) } throws DataIntegrityViolationException("중복 키 예외 발생")

        // when
        val e = assertThrows<BusinessException> { feedService.createFavoriteFeed(user, 1L) }

        // then
        assertThat(e.errorType.code).isEqualTo(40002)
    }

    @Test
    fun `없는 피드에 좋아요를 누르면 예외가 발생한다`() {
        // given
        every { feedRepository.findByIdOrNull(1L) } returns null

        // when, then
        assertThrows(FeedNotFoundException::class.java) {
            feedService.createFavoriteFeed(user, 1L)
        }
    }

    @Test
    fun `피드 좋아요가 삭제된다`() {
        // given
        every { favoriteFeedRepository.deleteByUserIdAndFeedId(1L, 1L) } returns Unit

        // when, then
        assertDoesNotThrow { feedService.deleteFavoriteFeed(user, 1L) }
    }
}
