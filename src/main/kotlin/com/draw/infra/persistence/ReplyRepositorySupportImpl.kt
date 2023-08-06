package com.draw.infra.persistence

import com.draw.domain.feed.Feed
import com.draw.domain.feed.QFeed
import com.draw.domain.reply.QBlockReply
import com.draw.domain.reply.QReply
import com.draw.domain.reply.Reply
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl

class ReplyRepositorySupportImpl(
    private val queryFactory: JPAQueryFactory,
) : ReplyRepositorySupport {
    override fun findWriterReplies(writerId: Long, lastReplyId: Long?): Slice<Reply> {
        val feed = QFeed.feed

        val results = queryFactory.selectFrom(reply)
            .join(reply.feed, feed).fetchJoin()
            .where(
                ltReplyId(lastReplyId),
                reply.writerId.eq(writerId)
            )
            .orderBy(reply.id.desc())
            .limit((REPLY_PAGE_SIZE + 1).toLong())
            .fetch()

        return resultsToSlice(results)
    }

    override fun findAllByFeedAndBlockExclude(feed: Feed, userId: Long): List<Reply> {
        val blockReply = QBlockReply.blockReply

        return queryFactory.selectFrom(reply)
            .leftJoin(blockReply)
            .on(
                reply.eq(blockReply.reply)
                    .and(blockReply.userId.eq(userId))
            )
            .where(
                reply.feed.eq(feed),
                blockReply.id.isNull,
            )
            .fetch()
    }

    private fun resultsToSlice(results: MutableList<Reply>): SliceImpl<Reply> {
        if (results.size > REPLY_PAGE_SIZE) {
            return SliceImpl(results.subList(0, REPLY_PAGE_SIZE), Pageable.unpaged(), true)
        }
        return SliceImpl(results, Pageable.unpaged(), false)
    }

    private fun ltReplyId(lastReplyId: Long?) = lastReplyId?.let { reply.id.lt(lastReplyId) }

    companion object {
        private val reply = QReply.reply
        private const val REPLY_PAGE_SIZE = 20
    }
}
