package com.draw.common.enums

import com.draw.domain.Point
import com.draw.domain.user.User

enum class PromotionType(val title: String, val grantPoint: Point) {
    WELCOME("웰컴 선물이에요!", Point(500L)),
    DAILY_ATTENDANCE("반가워요!", Point(100L)),
    
    ;

    fun toResult(user: User): Result {
        return Result(this, user)
    }

    class Result private constructor(
        val promotionType: PromotionType,
        val title: String,
        val grantedPoint: Point,
        val asIsPoint: Point,
        val toBePoint: Point,
    ) {
        constructor(type: PromotionType, user: User) :
            this(type, type.title, type.grantPoint, user.getPoint(), type.grantPoint + user.getPoint())
    }
}
