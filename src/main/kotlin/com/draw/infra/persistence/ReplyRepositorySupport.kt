package com.draw.infra.persistence

import com.draw.domain.feed.Feed
import com.draw.domain.reply.Reply

interface ReplyRepositorySupport {

    fun findAllByFeedAndBlockExclude(feed: Feed, userId: Long): List<Reply>
}
