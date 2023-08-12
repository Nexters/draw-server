package com.draw.controller.dto

import com.draw.common.enums.PromotionType
import com.draw.domain.Point
import com.draw.domain.promotion.Promotion
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "프로모션 정보")
data class PromotionResultRes(
    @Schema(description = "프로모션 ID", example = "1")
    val id: Long,
    @Schema(description = "프로모션 타입", example = "WELCOME")
    val promotionType: PromotionType,
    @Schema(description = "프로모션 제목", example = "웰컴 선물이에요!")
    val title: String,
    @Schema(description = "부여할 포인트")
    val grantedPoint: Point,
    @Schema(description = "현재 유저가 가지고 있는 포인트")
    val asIsPoint: Point,
    @Schema(description = "부여 완료후 유저의 포인트")
    val toBePoint: Point,
) {
    companion object {
        fun of(promotion: Promotion): PromotionResultRes {
            val result = promotion.toResult()
            return PromotionResultRes(
                id = promotion.id,
                promotionType = result.promotionType,
                title = result.title,
                grantedPoint = result.grantedPoint,
                asIsPoint = result.asIsPoint,
                toBePoint = result.toBePoint,
            )
        }
    }
}
