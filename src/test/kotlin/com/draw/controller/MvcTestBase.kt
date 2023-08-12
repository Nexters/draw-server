package com.draw.controller

import com.draw.common.enums.OAuthProvider
import com.draw.domain.user.User
import com.draw.infra.persistence.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class MvcTestBase {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun saveUser(): User {
        return userRepository.save(User(oauthProvider = OAuthProvider.KAKAO, registrationCompleted = true))
    }
}
