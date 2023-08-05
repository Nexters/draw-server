package com.draw.infra.persistence

import com.draw.domain.feed.Feed
import com.draw.domain.reply.Reply
import org.springframework.data.domain.Slice

interface ReplyRepositorySupport {

    fun findWriterReplies(writerId: Long, lastReplyId: Long?): Slice<Reply>

    fun findAllByFeedAndBlockExclude(feed: Feed, userId: Long): List<Reply>
}
