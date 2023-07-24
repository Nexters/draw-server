package com.draw.controller.dto

import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI

data class RepliesRes(
    val replies: List<ReplyRes>,
    val hasNext: Boolean,
)

data class ReplyRes(
    val id: Long,
    val content: String,
    val status: ReplyStatus,
    val writerId: Long,
    val writer: ReplyWriterRes?,
)

data class ReplyWriterRes(
    val mbti: MBTI,
    val gender: Gender
)

enum class ReplyStatus {
    NORMAL, MINE, PEEKED
}