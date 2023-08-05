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
    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = false)
    val reply: Reply,

    @Column(nullable = false)
    val userId: Long
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
