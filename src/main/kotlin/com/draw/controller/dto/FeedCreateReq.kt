package com.draw.controller.dto

import com.draw.common.enums.AgeOption
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTIChar
import com.draw.common.enums.VisibleTarget
import com.draw.domain.feed.Feed

data class FeedCreateReq(
    val content: String,
    val genders: List<Gender> = emptyList(),
    val ageOption: AgeOption = AgeOption.ALL,
    val mbtiChars: List<MBTIChar> = emptyList(),
) {

    fun toEntity(userId: Long, userAge: Int): Feed {
        return Feed(
            content = content,
            writerId = userId,
            genders = genders.toMutableList(),
            ageRange = ageOption.toAgeRange(userAge),
            visibleTarget = VisibleTarget.of(userAge),
            mbtiChars = mbtiChars.toMutableList(),
        )
    }
}
