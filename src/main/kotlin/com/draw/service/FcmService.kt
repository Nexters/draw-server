package com.draw.service

import com.draw.domain.user.User

interface FcmService {
    fun pushLike(fromUser: User, receiveUser: User, detailId: Long)
    fun pushPeekNotification(peekUser: User, receiveUser: User, detailId: Long)
    fun pushFeedRecommendation(feedWriter: User, receiveUser: User, detailId: Long)
}
