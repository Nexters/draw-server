package com.draw.controller.dto

import com.draw.common.enums.AgeOption
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTIChar
import com.draw.domain.feed.Feed

data class FeedCreateReq(
    val content: String,
    val genders: List<Gender> = emptyList(),
    val ageOption: AgeOption = AgeOption.ALL,
    val mbtiChars: List<MBTIChar> = emptyList(),
) {

    // TODO: user 객체 추가시 변경 2023/08/03 (koi)
    fun toEntity(userId: Long, userIntAge: Int): Feed {
        return Feed(
            content = content,
            writerId = userId,
            genders = genders.toMutableList(),
            ageRange = ageOption.toAgeRange(userIntAge),
            mbtiChars = mbtiChars.toMutableList(),
        )
    }
}
