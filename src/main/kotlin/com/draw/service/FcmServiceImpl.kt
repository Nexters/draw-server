package com.draw.service

import com.draw.domain.user.User
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("!test")
@Service
class FcmServiceImpl(
    private val firebaseMessaging: FirebaseMessaging,
) : FcmService {
    override fun pushLike(fromUser: User, receiveUser: User, detailId: Long) {
        if (!canReceivePush(receiveUser)) {
            return
        }

        val notification =
            createNotification(
                "${fromUser.gender!!.description}, ${fromUser.getAge()}, ${fromUser.mbti} 님이",
                "좋아해요",
            )
        val message = createMessage(receiveUser, notification)
        firebaseMessaging.send(message)
    }

    override fun pushPeekNotification(peekUser: User, receiveUser: User, detailId: Long) {
        if (!canReceivePush(receiveUser)) {
            return
        }

        val notification =
            createNotification(
                "${peekUser.gender!!.description}, ${peekUser.getAge()}, ${peekUser.mbti} 님이",
                "당신의 프로필을 조회했어요!",
            )
        val message = createMessage(receiveUser, notification)
        firebaseMessaging.send(message)
    }

    override fun pushFeedRecommended(receiveUser: User, detailId: Long) {
        if (!canReceivePush(receiveUser)) {
            return
        }
    }

    private fun createNotification(title: String, body: String): Notification {
        return Notification.builder()
            .setTitle(title).setBody(body).build()
    }

    private fun createMessage(receiveUser: User, notification: Notification): Message {
        return Message.builder()
            .putData("url", "https://draw-nexters.netlify.app/question-detail/1")
            .setToken(receiveUser.fcmToken)
            .setNotification(notification)
            .build()
    }

    private fun createMessage(fcmToken: String, notification: Notification): Message {
        return Message.builder()
            .putData("url", "https://draw-nexters.netlify.app/question-detail/1")
            .setToken(fcmToken)
            .setNotification(notification)
            .build()
    }

    private fun canReceivePush(receiveUser: User): Boolean {
        return receiveUser.fcmToken != null
    }
}
