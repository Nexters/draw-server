package com.draw.infra.persistence.user

import com.draw.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>, UserCustomRepository {
    fun findByKakaoId(kakaoId: String): User?
    fun findByAppleId(appleId: String): User?
}
