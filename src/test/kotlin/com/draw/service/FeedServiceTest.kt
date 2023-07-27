package com.draw.service

import com.draw.domain.feed.FavoriteFeed
import com.draw.domain.feed.Feed
import com.draw.infra.persistence.FavoriteFeedRepository
import com.draw.infra.persistence.FeedRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(value = [MockKExtension::class])
class FeedServiceTest {

    private val feedRepository = mockk<FeedRepository>()
    private val favoriteFeedRepository = mockk<FavoriteFeedRepository>(relaxUnitFun = true)

    private val feedService = FeedService(feedRepository, favoriteFeedRepository)

    @Test
    fun `피드 좋아요가 생성된다`() {
        // given
        val feed = Feed(content = "content", writerId = 1L)
        every { feedRepository.findByIdOrNull(1L) } returns feed
        every { favoriteFeedRepository.save(any()) } returns FavoriteFeed(
            userId = 1L,
            feed = feed
        )

        // when
        feedService.createFavoriteFeed(1L, 1L)

        // then
        verify { favoriteFeedRepository.save(any()) }
    }

    @Test
    fun `한 피드에 중복 좋아요가 유입되어도 예외가 발생하지 않는다`() {
        // given
        val feed = Feed(content = "content", writerId = 1L)
        every { feedRepository.findByIdOrNull(1L) } returns feed
        every { favoriteFeedRepository.save(any()) } throws DuplicateKeyException("중복 키 예외 발생")

        // when, then
        assertDoesNotThrow { feedService.createFavoriteFeed(1L, 1L) }
    }

    @Test
    fun `없는 피드에 좋아요를 누르면 예외가 발생한다`() {
        // given
        every { feedRepository.findByIdOrNull(1L) } returns null

        // when, then
        assertThrows(IllegalArgumentException::class.java) {
            feedService.createFavoriteFeed(1L, 1L)
        }
    }

    @Test
    fun `피드 좋아요가 삭제된다`() {
        // given
        every { favoriteFeedRepository.deleteByUserIdAndFeedId(1L, 1L) } returns Unit

        // when, then
        assertDoesNotThrow { feedService.deleteFavoriteFeed(1L, 1L) }
    }
}
