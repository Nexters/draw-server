package com.draw.controller.oauth

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * 로컬에서 카카오 JS-SDK 연동을 테스트하기 위한 임시 컨트롤러
 */
@Controller
@RequestMapping("/auth/v1/test")
class TestController(
    @Value("\${kakao.auth.js-key}") private val kakaoJsKey: String,
) {
    @GetMapping
    fun login(model: Model): String {
        model.addAttribute("kakaoJsKey", kakaoJsKey)
        return "index"
    }
}
