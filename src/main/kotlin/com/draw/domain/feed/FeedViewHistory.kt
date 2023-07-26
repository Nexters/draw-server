package com.draw.domain.feed

import com.draw.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class FeedViewHistory(
    userId: Long,
    feedId: Long,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var userId: Long = userId
        protected set

    @Column(nullable = false)
    var feedId: Long = feedId
        protected set
}
