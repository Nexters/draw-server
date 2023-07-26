package com.draw.controller.dto

data class FeedsRes(
    val feeds: List<FeedRes>,
    val hasNext: Boolean
)

data class FeedRes(
    val id: Long,
    val content: String,
    val favoriteCount: Int,
    val isFit: Boolean,
)
