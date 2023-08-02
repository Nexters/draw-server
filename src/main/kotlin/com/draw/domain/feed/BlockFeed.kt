package com.draw.domain.feed

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class BlockFeed(
    feed: Feed,
    userId: Long,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    var feed: Feed = feed
        protected set

    @Column(nullable = false)
    var userId: Long = userId
        protected set
}
