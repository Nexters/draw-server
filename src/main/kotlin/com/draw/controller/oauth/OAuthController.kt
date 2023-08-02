package com.draw.controller.oauth

import com.draw.common.Const
import com.draw.common.enums.OAuthProvider
import com.draw.controller.dto.AppleLoginReq
import com.draw.controller.dto.OAuthReq
import com.draw.controller.dto.TokenRefreshReq
import com.draw.controller.dto.TokenRefreshRes
import com.draw.service.oauth.AppleOAuthService
import com.draw.service.oauth.CommonOAuthService
import com.draw.service.oauth.KakaoOAuthService
import com.draw.service.oauth.dto.LoginResult
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/v1")
class OAuthController(
    private val commonOAuthService: CommonOAuthService,
    private val appleOAuthService: AppleOAuthService,
    private val kakaoOAuthService: KakaoOAuthService,
) {

    @PostMapping("/login")
    @Operation(summary = "공통 Oauth 로그인 콜백", description = Const.AUTH_TAG)
    fun login(
        @RequestBody req: OAuthReq,
    ): LoginResult {
        return if (req.provider == OAuthProvider.KAKAO) {
            kakaoOAuthService.registerOrLogin(req.code)
        } else {
            appleOAuthService.registerOrLogin(req.code)
        }
    }

    @GetMapping("/local/kakao/login")
    @Operation(summary = "로컬 테스트용 카카오 Oauth 로그인 콜백", description = Const.AUTH_TAG)
    fun localKakaoLoginCallBack(
        @RequestParam("code") code: String,
    ): LoginResult {
        return kakaoOAuthService.registerOrLogin(code)
    }

    @PostMapping("/local/apple/login")
    @Operation(summary = "로컬 테스트용 애플 Oauth 로그인 콜백", description = Const.AUTH_TAG)
    fun localAppleLoginCallBack(
        @RequestBody req: AppleLoginReq,
    ): LoginResult {
        return appleOAuthService.registerOrLogin(req.idToken)
    }

    @PostMapping("/token/refresh")
    @Operation(summary = "액세스 토큰 리프레시", description = Const.AUTH_TAG)
    fun refreshAccessToken(
        @RequestBody req: TokenRefreshReq,
    ): TokenRefreshRes {
        val result = commonOAuthService.refreshToken(req.accessToken, req.refreshToken)
        return TokenRefreshRes(accessToken = result.accessToken, refreshToken = result.refreshToken)
    }
}
