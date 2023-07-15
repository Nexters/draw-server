package com.draw.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController {

    @GetMapping("/")
    fun example(): String {
        return "드로우 프로젝트 시작! - v2"
    }
}
