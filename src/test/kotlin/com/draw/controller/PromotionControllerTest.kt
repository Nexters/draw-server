package com.draw.controller

import com.draw.common.enums.OAuthProvider
import com.draw.common.enums.PromotionType
import com.draw.component.JwtProvider
import com.draw.domain.promotion.Promotion
import com.draw.domain.promotion.PromotionRepository
import com.draw.domain.user.User
import com.draw.infra.persistence.user.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class PromotionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var promotionRepository: PromotionRepository

    @Autowired
    private lateinit var jwtProvider: JwtProvider

    @Test
    fun `유저의 사용하지 않은 모든 프로모션 리스트를 반환한다`() {
        val user = saveUser()
        promotionRepository.saveAll(
            listOf(
                Promotion(user = user, promotionType = PromotionType.WELCOME),
                Promotion(user = user, promotionType = PromotionType.DAILY_ATTENDANCE).apply { consume() },
            ),
        )
        val token = jwtProvider.generateAccessToken(user)
        mockMvc.perform(
            get("/api/v1/promotions")
                .header("Authorization", "Bearer $token"),
        )
            .andExpect(status().isOk)
            .andExpect {
                jsonPath("$.length()").value(1)
                jsonPath("$.[0].['promotionType']").value(PromotionType.WELCOME.name)
            }
    }

    @Test
    fun `프로모션을 사용한다`() {
        val user = saveUser()
        val promotion = promotionRepository.save(
            Promotion(user = user, promotionType = PromotionType.WELCOME),
        )
        val token = jwtProvider.generateAccessToken(user)
        mockMvc.perform(
            post("/api/v1/promotions/consume/${promotion.id}")
                .header("Authorization", "Bearer $token"),
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `이미 사용한 프로모션을 중복 사용하면 400을 반환한다`() {
        val user = saveUser()
        val promotion = promotionRepository.save(
            Promotion(user = user, promotionType = PromotionType.WELCOME).apply { consume() },
        )
        val token = jwtProvider.generateAccessToken(user)
        mockMvc.perform(
            post("/api/v1/promotions/consume/${promotion.id}")
                .header("Authorization", "Bearer $token"),
        )
            .andExpect(status().`is`(400))
    }

    private fun saveUser(): User {
        return userRepository.save(User(oauthProvider = OAuthProvider.KAKAO, registrationCompleted = true))
    }
}
