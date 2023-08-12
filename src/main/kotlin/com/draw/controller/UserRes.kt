package com.draw.controller

import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI

data class UserRes(
    val id: Long,
    val gender: Gender,
    val mbti: MBTI,
    val age: Int,
    val point: Long,
)
