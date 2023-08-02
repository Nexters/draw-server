package com.draw.infra.persistence

import com.draw.domain.feed.Feed
import com.draw.domain.reply.QBlockReply
import com.draw.domain.reply.QReply
import com.draw.domain.reply.Reply
import com.querydsl.jpa.impl.JPAQueryFactory

class ReplyRepositorySupportImpl(
    private val queryFactory: JPAQueryFactory,
) : ReplyRepositorySupport {

    override fun findAllByFeedAndBlockExclude(feed: Feed, userId: Long): List<Reply> {
        val blockReply = QBlockReply.blockReply

        return queryFactory.selectFrom(reply)
            .leftJoin(blockReply)
            .on(reply.eq(blockReply.reply))
            .where(
                reply.feed.eq(feed),
                blockReply.id.isNull,
            )
            .fetch()
    }

    companion object {
        private val reply = QReply.reply
    }
}
