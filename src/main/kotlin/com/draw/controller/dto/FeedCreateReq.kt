package com.draw.controller.dto

import com.draw.common.enums.AgeRange
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTIChar

data class FeedCreateReq(
    val content: String,
    val genders: List<Gender> = emptyList(),
    val ageRange: AgeRange = AgeRange.ALL,
    val mbtiChars: List<MBTIChar> = emptyList(),
)
