package com.draw.service.dto

import com.draw.common.enums.AgeRange
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTIChar
import com.querydsl.core.annotations.QueryProjection
import java.time.ZonedDateTime

data class FeedDtoProjection @QueryProjection constructor(
    val id: Long,
    val content: String,
    val isFavorite: Boolean,
    val favoriteCount: Int,
    val genders: MutableList<Gender>, // 맞춤 피드용도
    val ageRange: AgeRange, // 맞춤 피드용도
    val mbtiChars: MutableList<MBTIChar>, // 맞춤 피드용도
    val createdAt: ZonedDateTime, // 정렬용도
)
