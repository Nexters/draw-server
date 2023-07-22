package com.draw.domain.feed

import com.draw.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotNull

@Entity
class FavoriteFeed : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "feed_id")
    @NotNull
    var feed: Feed? = null

    var userId: Long? = null // TODO: entity 2023/07/23 (koi)
}
