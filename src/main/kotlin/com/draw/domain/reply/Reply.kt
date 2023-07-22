package com.draw.domain.reply

import com.draw.domain.common.BaseEntity
import com.draw.domain.feed.Feed
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Reply : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var userId: Long? = null

    @ManyToOne
    @JoinColumn(name = "feed_id")
    var feed: Feed? = null

    var content: String? = null
}
