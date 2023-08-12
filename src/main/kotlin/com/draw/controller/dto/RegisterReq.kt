package com.draw.controller.dto

import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "유저 가입정보")
data class RegisterReq(
    @Schema(description = "생년월일", example = "961012")
    val birthday: String,
    @Schema(description = "성별", example = "MALE 혹은 FEMALE")
    val gender: Gender,
    @Schema(description = "mbti", example = "INFJ")
    val mbti: MBTI,
)
