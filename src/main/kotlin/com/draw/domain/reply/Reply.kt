package com.draw.domain.reply

import com.draw.domain.common.BaseEntity
import com.draw.domain.feed.Feed
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Reply(
    feed: Feed,
    content: String,
    writerId: Long,
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
}
