package com.draw.controller.promotion

import com.draw.controller.dto.PromotionConsumeReq
import com.draw.controller.dto.PromotionResultRes
import com.draw.domain.user.User
import com.draw.service.promotion.PromotionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/promotions")
class PromotionController(
    private val promotionService: PromotionService,
) {

    @GetMapping
    @Operation(summary = "사용하지 않은 프로모션 반환", description = "사용하지 않은 프로모션 리스트를 반환한다")
    fun getPromotions(
        @AuthenticationPrincipal user: User,
    ): List<PromotionResultRes> {
        return promotionService.listAllUnconsumedPromotions(user).map { PromotionResultRes.of(it) }
    }

    @PostMapping("/consume")
    @Operation(summary = "프로모션 사용", description = "요청을 보낸 유저가 프로모션을 명시적으로 사용한다")
    fun consumePromotion(
        @AuthenticationPrincipal user: User,
        @RequestBody req: PromotionConsumeReq,
    ) {
        req.promotionIds.forEach {
            promotionService.consume(user, it)
        }
    }
}
