package com.draw.service

import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
import com.draw.domain.user.DateOfBirth
import com.draw.domain.user.User
import com.draw.infra.persistence.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
) {
    fun register(user: User, info: UserUpdateInfo) {
        user.dateOfBirth = info.dateOfBirth.value
        user.gender = info.gender
        user.mbti = info.mbti
        user.registrationCompleted = true
        userRepository.save(user)
    }

    fun registerFcmToken(user: User, fcmToken: String) {
        user.fcmToken = fcmToken
        userRepository.save(user)
    }
}

data class UserUpdateInfo(
    val dateOfBirth: DateOfBirth,
    val gender: Gender,
    val mbti: MBTI,
)
