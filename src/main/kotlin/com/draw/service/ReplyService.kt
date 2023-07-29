package com.draw.service

import com.draw.controller.dto.MyRepliesRes
import com.draw.controller.dto.MyReplyRes
import com.draw.infra.persistence.ReplyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReplyService(
    private val replyRepository: ReplyRepository,
) {

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
