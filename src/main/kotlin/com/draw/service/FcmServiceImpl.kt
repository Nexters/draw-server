package com.draw.service

import com.draw.domain.user.User
import com.draw.infra.persistence.user.UserRepository
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("!test")
@Service
class FcmServiceImpl(
    private val firebaseMessaging: FirebaseMessaging,
    private val userRepository: UserRepository,
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
        val message = createMessage(receiveUser, notification, detailId)
        sendMessage(message)
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
        val message = createMessage(receiveUser, notification, detailId)
        sendMessage(message)
    }

    override fun pushFeedRecommendation(feedWriter: User, receiveUser: User, detailId: Long) {
        if (!canReceivePush(receiveUser) || !receiveUser.isFitRecommendationIntervalElapsed()) {
            return
        }
        val notification =
            createNotification(
                "From ${feedWriter.gender!!.description}, ${feedWriter.getAge()}, ${feedWriter.mbti}",
                "질문이 도착했어요!",
            )
        val message = createMessage(receiveUser, notification, detailId)
        receiveUser.markFitRecommendationReceived()
        sendMessage(message)
        userRepository.save(receiveUser)
    }

    private fun createNotification(title: String, body: String): Notification {
        return Notification.builder()
            .setTitle(title).setBody(body).build()
    }

    private fun createMessage(receiveUser: User, notification: Notification, detailId: Long): Message {
        return Message.builder()
            .putData("url", "https://draw-nexters.netlify.app/question-detail/$detailId")
            .setToken(receiveUser.fcmToken)
            .setNotification(notification)
            .build()
    }

    private fun canReceivePush(receiveUser: User): Boolean {
        return receiveUser.fcmToken != null
    }

    private fun sendMessage(firebaseMessage: Message) {
        runCatching {
            firebaseMessaging.send(firebaseMessage)
        }.onFailure {
            Sentry.captureEvent(
                SentryEvent(it).apply {
                    message = io.sentry.protocol.Message().apply {
                        message = it.message
                    }
                    level = SentryLevel.WARNING
                },
            )
        }
    }
}
