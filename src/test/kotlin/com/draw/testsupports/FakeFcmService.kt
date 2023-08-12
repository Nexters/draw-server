package com.draw.testsupports

import com.draw.domain.user.User
import com.draw.service.FcmService
import org.springframework.stereotype.Service

@Service
class FakeFcmService : FcmService {
    override fun pushLike(fromUser: User, receiveUser: User, detailId: Long) {
    }

    override fun pushPeekNotification(peekUser: User, receiveUser: User, detailId: Long) {
    }

    override fun pushFeedRecommended(receiveUser: User, detailId: Long) {
    }
}
