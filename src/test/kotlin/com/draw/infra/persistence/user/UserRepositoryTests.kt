package com.draw.infra.persistence.user

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserRepositoryTests {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `삭제되지 않은 유저중에 카카오 ID 가 매치하는 유저를 조회한다`() {
        userRepository.findByKakaoId("0")
    }
}
