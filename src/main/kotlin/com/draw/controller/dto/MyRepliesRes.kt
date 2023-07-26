package com.draw.controller.dto

data class MyRepliesRes(
    val myReplies: List<MyReplyRes>,
    val hasNext: Boolean,
)

data class MyReplyRes(
    val feedId: Long,
    val feedContent: String,
    val replyId: Long,
    val replyContent: String,
)
