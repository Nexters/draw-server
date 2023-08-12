package com.draw.controller

import com.draw.controller.dto.RegisterReq
import com.draw.domain.user.DateOfBirth
import com.draw.domain.user.User
import com.draw.service.UserService
import com.draw.service.UserUpdateInfo
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/register")
    @Operation(summary = "회원정보 입력 및 가입완료")
    fun register(
        @AuthenticationPrincipal user: User,
        @RequestBody req: RegisterReq,
    ) {
        userService.register(user, UserUpdateInfo(DateOfBirth(req.birthday), req.gender, req.mbti))
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회")
    fun getMyInfo(
        @AuthenticationPrincipal user: User,
    ): UserRes {

        return UserRes(
            id = user.id!!,
            gender = user.gender!!,
            mbti = user.mbti!!,
            age = user.getAge(),
            point = user.getPoint().value,
        )
    }
}
