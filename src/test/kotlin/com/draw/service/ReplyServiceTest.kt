package com.draw.service

import com.draw.controller.dto.ReplyCreateReq
import com.draw.domain.feed.Feed
import com.draw.domain.reply.Reply
import com.draw.infra.persistence.FeedRepository
import com.draw.infra.persistence.ReplyRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(value = [MockKExtension::class])
class ReplyServiceTest {

    private val feedRepository = mockk<FeedRepository>()
    private val replyRepository = mockk<ReplyRepository>()

    private val replyService = ReplyService(feedRepository, replyRepository)

    @Test
    fun `피드내 리플이 생성된다`() {
        // given
        val feed = Feed(content = "content", writerId = 1L)
        every { feedRepository.findByIdOrNull(1L) } returns feed

        // when
        replyService.createReply(1L, 1L, ReplyCreateReq(content = "content"))

        // then
        assertThat(feed.replies).hasSize(1)
    }

    @Test
    fun `리플 차단이 생성된다`() {
        // given
        val reply = Reply(feed = Feed(content = "feed-content", writerId = 1L), content = "content", writerId = 1L)
        every { replyRepository.findByIdOrNull(1L) } returns reply

        // when
        replyService.blockReply(1L, 1L)

        // then
        assertThat(reply.blockReplies).hasSize(1)
    }

    @Test
    fun `리플 신고시에도 차단이 생성된다`() {
        // given
        val reply = Reply(feed = Feed(content = "feed-content", writerId = 1L), content = "content", writerId = 1L)
        every { replyRepository.findByIdOrNull(1L) } returns reply

        // when
        replyService.claimReply(1L, 1L)

        // then
        assertThat(reply.blockReplies).hasSize(1)
    }
}
