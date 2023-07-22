package com.draw.domain.reply

import com.draw.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class PeekReply : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var userId: Long? = null // TODO:  2023/07/23 (koi)

    @ManyToOne
    var reply: Reply? = null
}
