package com.draw.domain.reply

import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
import com.fasterxml.jackson.annotation.JsonProperty

data class WriterInfo(
    @JsonProperty("mbti")
    val mbti: MBTI,
    @JsonProperty("gender")
    val gender: Gender,
    @JsonProperty("age")
    val age: Int,
)
