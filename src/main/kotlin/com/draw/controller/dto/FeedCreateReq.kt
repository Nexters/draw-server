package com.draw.controller.dto

import com.draw.common.enums.AgeRange
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTIChar
import com.draw.domain.feed.Feed

data class FeedCreateReq(
    val content: String,
    val genders: List<Gender> = emptyList(),
    val ageRange: AgeRange = AgeRange.ALL,
    val mbtiChars: List<MBTIChar> = emptyList(),
) {

    fun toEntity(userId: Long): Feed {
        return Feed(
            content = content,
            writerId = userId,
            genders = genders.toMutableList(),
            ageRange = ageRange,
            mbtiChars = mbtiChars.toMutableList(),
        )
    }
}
