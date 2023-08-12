package com.draw.domain.reply

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
class PeekReply(
    userId: Long,
    reply: Reply,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var userId: Long = userId // TODO:  2023/07/23 (koi)
        protected set

    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var reply: Reply = reply
        protected set
}
