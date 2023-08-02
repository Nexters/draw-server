package com.draw.domain.feed

import com.draw.common.enums.AgeRange
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTIChar
import com.draw.domain.common.BaseEntity
import com.draw.domain.common.converter.GendersConverter
import com.draw.domain.common.converter.MBTICharsConverter
import com.draw.domain.reply.Reply
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.hibernate.annotations.Formula

@Entity
class Feed(
    content: String,
    writerId: Long,
    genders: MutableList<Gender> = mutableListOf(),
    ageRange: AgeRange = AgeRange.ALL,
    mbtiChars: MutableList<MBTIChar> = mutableListOf(),
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var content: String = content
        protected set

    @Column(nullable = false)
    var writerId: Long = writerId
        protected set

    @Column(nullable = false)
    @Convert(converter = GendersConverter::class)
    var genders: MutableList<Gender> = genders
        protected set

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var ageRange: AgeRange = ageRange
        protected set

    @Column(nullable = false)
    @Convert(converter = MBTICharsConverter::class)
    var mbtiChars: MutableList<MBTIChar> = mbtiChars
        protected set

    @OneToMany(
        mappedBy = "feed",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST],
    )
    protected val mutableReplies: MutableList<Reply> = mutableListOf()
    val replies: List<Reply> get() = mutableReplies

    @OneToMany(
        mappedBy = "feed",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.REMOVE],
        orphanRemoval = true
    )
    protected val mutableFeedViewHistories: MutableList<FeedViewHistory> = mutableListOf()
    val feedViewHistories: List<FeedViewHistory> get() = mutableFeedViewHistories

    @OneToMany(
        mappedBy = "feed",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST],
    )
    protected val mutableBlockFeeds: MutableList<BlockFeed> = mutableListOf()
    val blockFeeds: List<BlockFeed> get() = mutableBlockFeeds

    @Formula("(select count(*) from favorite_feed ff where ff.feed_id = id)")
    var favoriteCount: Int = 0
        protected set

    fun addReply(userId: Long, content: String) {
        val reply = Reply(feed = this, writerId = userId, content = content)
        mutableReplies.add(reply)
    }

    fun addFeedViewHistory(userId: Long) {
        val feedViewHistory = FeedViewHistory(userId = userId, feed = this)
        mutableFeedViewHistories.add(feedViewHistory)
    }

    fun addBlockFeed(userId: Long) {
        val blockFeed = BlockFeed(feed = this, userId = userId)
        mutableBlockFeeds.add(blockFeed)
    }
}
