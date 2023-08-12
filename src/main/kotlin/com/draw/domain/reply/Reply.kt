package com.draw.domain.reply

import com.draw.domain.common.BaseEntity
import com.draw.domain.common.converter.WriterInfoConverter
import com.draw.domain.feed.Feed
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class Reply(
    feed: Feed,
    content: String,
    writerId: Long,
    writerInfo: WriterInfo,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    var feed: Feed = feed
        protected set

    @Column(length = 256, nullable = false)
    var content: String = content
        protected set

    @Column(nullable = false)
    var writerId: Long = writerId
        protected set

    @Column(nullable = false, columnDefinition = "json")
    @Convert(converter = WriterInfoConverter::class)
    var writerInfo: WriterInfo = writerInfo
        protected set

    @OneToMany(
        mappedBy = "reply",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST],
    )
    protected val mutableBlockReplies: MutableList<BlockReply> = mutableListOf()
    val blockReplies: List<BlockReply> get() = mutableBlockReplies

    fun addBlockReply(userId: Long) {
        val blockReply = BlockReply(reply = this, userId = userId)
        mutableBlockReplies.add(blockReply)
    }
}
