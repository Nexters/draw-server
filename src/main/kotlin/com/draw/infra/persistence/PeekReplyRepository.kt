package com.draw.infra.persistence

import com.draw.domain.reply.PeekReply
import com.draw.domain.reply.Reply
import org.springframework.data.jpa.repository.JpaRepository

interface PeekReplyRepository : JpaRepository<PeekReply, Long> {
    fun findAllByUserIdAndReplyIn(userId: Long, replies: List<Reply>): List<PeekReply>
}
