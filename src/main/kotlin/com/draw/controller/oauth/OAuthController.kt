package com.draw.controller.oauth

import com.draw.common.Const
import com.draw.controller.dto.TokenRefreshReq
import com.draw.controller.dto.TokenRefreshRes
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
    private val kakaoOAuthService: KakaoOAuthService,
) {

    @GetMapping("/kakao/login")
    @Operation(summary = "카카오 OAuth 로그인 콜백", description = Const.AUTH_TAG)
    fun loginCallBack(
        @RequestParam("code") code: String,
    ): LoginResult {
        return kakaoOAuthService.registerOrLogin(code)
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
