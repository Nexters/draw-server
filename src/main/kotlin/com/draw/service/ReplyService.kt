package com.draw.service

import com.draw.controller.dto.MyRepliesRes
import com.draw.controller.dto.MyReplyRes
import com.draw.controller.dto.ReplyCreateReq
import com.draw.infra.persistence.FeedRepository
import com.draw.infra.persistence.ReplyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReplyService(
    private val feedRepository: FeedRepository,
    private val replyRepository: ReplyRepository,
) {
    @Transactional
    fun createReply(userId: Long, feedId: Long, reqReplyCreateReq: ReplyCreateReq) {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw IllegalArgumentException("존재하지 않는 피드입니다.")
        feed.addReply(userId, reqReplyCreateReq.content)
    }

    @Transactional
    fun blockReply(userId: Long, replyId: Long) {
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw IllegalArgumentException("존재하지 않는 리플입니다.")
        reply.addBlockReply(userId)
    }

    @Transactional
    fun claimReply(userId: Long, replyId: Long) {
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw IllegalArgumentException("존재하지 않는 리플입니다.")
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
}
