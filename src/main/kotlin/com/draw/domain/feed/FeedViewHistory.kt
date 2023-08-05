package com.draw.domain.feed

import com.draw.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class FeedViewHistory(
    @Column(nullable = false)
    val userId: Long,

    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    val feed: Feed,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
