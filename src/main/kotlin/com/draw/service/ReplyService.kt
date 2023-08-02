package com.draw.service

import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
import com.draw.common.exception.FeedNotFoundException
import com.draw.common.exception.ReplyNotFoundException
import com.draw.controller.dto.MyRepliesRes
import com.draw.controller.dto.MyReplyRes
import com.draw.controller.dto.RepliesRes
import com.draw.controller.dto.ReplyCreateReq
import com.draw.controller.dto.ReplyRes
import com.draw.controller.dto.ReplyStatus
import com.draw.controller.dto.ReplyWriterRes
import com.draw.domain.reply.Reply
import com.draw.infra.persistence.FeedRepository
import com.draw.infra.persistence.PeekReplyRepository
import com.draw.infra.persistence.ReplyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReplyService(
    private val feedRepository: FeedRepository,
    private val replyRepository: ReplyRepository,
    private val peekReplyRepository: PeekReplyRepository,
) {
    fun getReplies(inputUserId: Long?, feedId: Long): RepliesRes {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        val replies = inputUserId?.let { replyRepository.findAllByFeedAndBlockExclude(feed, it) } ?: feed.replies

        // 쿼리량이 많지 않을 것으로 보여져, n+1 쿼리 발생을 허용함, 추후 캐싱 적용 예정
        val replyReplyWriterResMap = inputUserId?.let { userId ->
            peekReplyRepository.findAllByUserIdAndReplyIn(userId, replies)
                .associateBy(
                    { it.reply },
                    { ReplyWriterRes(MBTI.ESTJ, Gender.MALE) },
                ) // TODO: userId 기반 user 정보 조회 2023/08/02 (koi)
        } ?: emptyMap()

        return RepliesRes(
            replies = replies.sortedByDescending { it.createdAt }
                .map { reply ->
                    ReplyRes(
                        id = reply.id!!,
                        content = reply.content,
                        status = replyReplyWriterResMap.getStatus(reply, inputUserId),
                        writerId = reply.writerId,
                        writer = replyReplyWriterResMap[reply],
                    )
                }.toList(),
        )
    }

    @Transactional
    fun createReply(userId: Long, feedId: Long, reqReplyCreateReq: ReplyCreateReq) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        feed.addReply(userId, reqReplyCreateReq.content)
    }

    @Transactional
    fun blockReply(userId: Long, replyId: Long) {
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw ReplyNotFoundException()
        reply.addBlockReply(userId)
    }

    @Transactional
    fun claimReply(userId: Long, replyId: Long) {
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw ReplyNotFoundException()
        reply.addBlockReply(userId)

        // TODO: claim 적재 로직 추가 2023/08/02 (koi)
    }

    // TODO: 페이징 이후 고민 2023/07/29 (koi)
    fun getMyReplies(userId: Long, lastReplyId: Long?): MyRepliesRes {
        val replies = replyRepository.findAllByWriterIdOrderByCreatedAtDesc(userId)

        return MyRepliesRes(
            myReplies = replies.map {
                MyReplyRes(
                    feedId = it.feed.id!!,
                    feedContent = it.feed.content,
                    replyId = it.id!!,
                    replyContent = it.content,
                )
            }.toList(),
            hasNext = false
        )
    }

    private fun Map<Reply, ReplyWriterRes>.getStatus(reply: Reply, inputUserId: Long?) =
        if (this.containsKey(reply)) ReplyStatus.PEEKED
        else if (reply.writerId == inputUserId) ReplyStatus.MINE
        else ReplyStatus.NORMAL
}
