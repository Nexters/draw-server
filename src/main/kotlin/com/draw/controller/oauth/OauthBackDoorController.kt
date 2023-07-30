package com.draw.controller.oauth

import com.draw.common.enums.OauthProvider
import com.draw.component.JwtProvider
import com.draw.domain.user.User
import com.draw.infra.persistence.user.UserRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/auth/v1/backdoor")
class OauthBackDoorController(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
) {
    @PostMapping("/register")
    @Operation(summary = "백도어 가입", description = "페이크 유저를 가입시킨다")
    fun register(@RequestBody req: BackDoorRegisterReq): BackDoorAuthRes {
        val user =
            userRepository.save(
                User(
                    kakaoId = if (req.oauthProvider == OauthProvider.KAKAO) "-1" else null,
                    appleId = if (req.oauthProvider == OauthProvider.APPLE) "-1" else null,
                    registrationCompleted = req.registerCompleted,
                    oauthProvider = req.oauthProvider,
                ),
            )
        val accessToken = jwtProvider.generateAccessToken(user, req.accessTokenLifeTime)
        user.refreshToken = jwtProvider.generateRefreshToken(user, req.refreshTokenLifeTime)
        userRepository.save(user)
        return BackDoorAuthRes(
            user = user,
            accessToken = accessToken,
            refreshToken = user.refreshToken!!,
        )
    }

    @PostMapping("/token")
    @Operation(summary = "백도어 로그인", description = "특정 id(pk) 의 유저의 토큰을 발급한다")
    fun login(@RequestBody req: BackDoorTokenReq): BackDoorAuthRes {
        val user =
            userRepository.findById(req.userId).getOrNull() ?: throw RuntimeException("id = ${req.userId} 가 존재하지 않습니다")
        val accessToken = jwtProvider.generateAccessToken(user, req.accessTokenLifeTime)
        user.refreshToken = jwtProvider.generateRefreshToken(user, req.refreshTokenLifeTime)
        userRepository.save(user)
        return BackDoorAuthRes(
            user = user,
            accessToken = accessToken,
            refreshToken = user.refreshToken!!,
        )
    }

    @Schema(description = "페이크 유저 생성 요청 객체")
    data class BackDoorRegisterReq(
        @Schema(description = "가입완료 여부")
        val registerCompleted: Boolean = false,
        @Schema(description = "Oauth 프로바이더")
        val oauthProvider: OauthProvider = OauthProvider.KAKAO,
        @Schema(description = "액세스 토큰 유효시간")
        val accessTokenLifeTime: Long? = null,
        @Schema(description = "리프레시 토큰 유효시간")
        val refreshTokenLifeTime: Long? = null,
    )

    @Schema(description = "페이크 유저 토큰발급 요청 객체")
    data class BackDoorTokenReq(
        @Schema(description = "로그인할 유저 ID")
        val userId: Long,
        @Schema(description = "액세스 토큰 유효시간")
        val accessTokenLifeTime: Long? = null,
        @Schema(description = "리프레시 토큰 유효시간")
        val refreshTokenLifeTime: Long? = null,
    )

    @Schema(description = "페이크 인증 응답 객체")
    data class BackDoorAuthRes(
        @Schema(description = "가입된 유저정보")
        val user: User,
        @Schema(description = "액세스 토큰")
        val accessToken: String,
        @Schema(description = "리프레시 토큰")
        val refreshToken: String,
    )
}
