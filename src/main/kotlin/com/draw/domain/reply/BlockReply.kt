package com.draw.domain.reply

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class BlockReply(
    reply: Reply,
    userId: Long,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = false)
    var reply: Reply = reply
        protected set

    @Column(nullable = false)
    var userId: Long = userId
        protected set
}
