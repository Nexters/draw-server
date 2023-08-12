package com.draw.service

import com.draw.domain.user.User

interface FcmService {
    fun pushLike(fromUser: User, receiveUser: User, detailId: Long)
    fun pushPeekNotification(peekUser: User, receiveUser: User, detailId: Long)
    fun pushFeedRecommended(receiveUser: User, detailId: Long)
}
