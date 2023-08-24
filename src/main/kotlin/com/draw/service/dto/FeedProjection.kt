package com.draw.service.dto

import com.draw.common.enums.AgeRange
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
import com.draw.common.enums.MBTIChar
import com.querydsl.core.annotations.QueryProjection
import java.time.ZonedDateTime

data class FeedProjection @QueryProjection constructor(
    val id: Long,
    val content: String,
    val favoriteId: Long? = null,
    val favoriteCount: Int,
    val genders: MutableList<Gender>, // 맞춤 피드용도
    val ageRange: AgeRange, // 맞춤 피드용도
    val mbtiChars: MutableList<MBTIChar>, // 맞춤 피드용도
    val writerId: Long,
    val createdAt: ZonedDateTime, // 정렬용도
) {

    fun isFavorite(): Boolean {
        return favoriteId != null
    }

    fun isFit(userGender: Gender, userAge: Int, userMBTI: MBTI): Boolean {
        if (!this.genders.contains(userGender)) {
            return false
        }

        val hasNonMatchMBTIChar = userMBTI.name
            .map { MBTIChar.valueOf(it.toString()) }
            .any { userMbtiChar -> existSamePosAndOppositeMBTIChar(userMbtiChar) }
        if (mbtiChars.isNotEmpty() && hasNonMatchMBTIChar) {
            return false
        }

        if (!ageRange.isInScope(userAge)) {
            return false
        }

        return true
    }

    // 동일한 pos의 MBTIChar이 하나라도 포함되어있고, 해당 MBTIChar이 포함되어있지 않은 경우
    private fun existSamePosAndOppositeMBTIChar(userMbtiChar: MBTIChar) =
        mbtiChars.any { it.pos == userMbtiChar.pos } && !this.mbtiChars.contains(userMbtiChar)
}
