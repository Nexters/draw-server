package com.draw.service

import com.draw.common.enums.VisibleTarget
import com.draw.controller.dto.ReplyCreateReq
import com.draw.domain.feed.Feed
import com.draw.domain.reply.Reply
import com.draw.infra.persistence.FeedRepository
import com.draw.infra.persistence.PeekReplyRepository
import com.draw.infra.persistence.ReplyRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(value = [MockKExtension::class])
class ReplyServiceTest {

    private val feedRepository = mockk<FeedRepository>()
    private val replyRepository = mockk<ReplyRepository>()
    private val peekReplyRepository = mockk<PeekReplyRepository>()

    private val replyService = ReplyService(feedRepository, replyRepository, peekReplyRepository)

    private lateinit var feed: Feed

    @BeforeEach
    fun setUp() {
        feed = Feed(content = "content", writerId = 1L, visibleTarget = VisibleTarget.ADULT)
            .apply { id = 1L }
    }


    @Test
    fun `피드내 리플이 생성된다`() {
        // given
        every { feedRepository.findByIdOrNull(1L) } returns feed

        // when
        replyService.createReply(1L, 1L, ReplyCreateReq(content = "content"))

        // then
        assertThat(feed.replies).hasSize(1)
    }

    @Test
    fun `리플 차단이 생성된다`() {
        // given
        val reply = Reply(feed = feed, content = "content", writerId = 1L)
        every { replyRepository.findByIdOrNull(1L) } returns reply

        // when
        replyService.blockReply(2L, 1L)

        // then
        assertThat(reply.blockReplies).hasSize(1)
    }

    @Test
    fun `내 리플은 차단할 수 없다`() {
        // given
        val reply = Reply(feed = feed, content = "content", writerId = 1L)
        every { replyRepository.findByIdOrNull(1L) } returns reply

        // when, then
        assertThrows<IllegalArgumentException> { replyService.blockReply(1L, 1L) }
    }

    @Test
    fun `리플 신고시에도 차단이 생성된다`() {
        // given
        val reply = Reply(feed = feed, content = "content", writerId = 1L)
        every { replyRepository.findByIdOrNull(1L) } returns reply

        // when
        replyService.claimReply(2L, 1L)

        // then
        assertThat(reply.blockReplies).hasSize(1)
    }

    @Test
    fun `내 리플은 신고할 수 없다`() {
        // given
        val reply = Reply(feed = feed, content = "content", writerId = 1L)
        every { replyRepository.findByIdOrNull(1L) } returns reply

        // when, then
        assertThrows<IllegalArgumentException> { replyService.claimReply(1L, 1L) }
    }

    @Test
    fun `내 리플은 훔쳐볼 수 없다`() {
        // given
        val reply = Reply(feed = feed, content = "content", writerId = 1L)
        every { replyRepository.findByIdOrNull(1L) } returns reply

        // when, then
        assertThrows<IllegalArgumentException> { replyService.peekReply(1L, 1L) }
    }
}
