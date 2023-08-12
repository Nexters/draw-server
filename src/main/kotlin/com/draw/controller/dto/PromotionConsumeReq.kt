package com.draw.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "프로모션 사용 요청")
data class PromotionConsumeReq(
    @Schema(description = "프로모션 아이디 리스트", example = "[1,2,3]")
    val promotionIds: List<Long>,
)
