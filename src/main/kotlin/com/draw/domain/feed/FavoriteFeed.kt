package com.draw.domain.feed

import com.draw.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class FavoriteFeed(
    feed: Feed,
    userId: Long,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var feed: Feed = feed
        protected set

    @Column(nullable = false)
    var userId: Long = userId // TODO: entity 2023/07/23 (koi)
        protected set
}
