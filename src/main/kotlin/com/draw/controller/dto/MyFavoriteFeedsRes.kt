package com.draw.controller.dto

data class MyFavoriteFeedsRes(
    val myFavoriteFeeds: List<MyFavoriteFeedRes>,
    val hasNext: Boolean
)

data class MyFavoriteFeedRes(
    val id: Long,
    val content: String,
    val favoriteId: Long,
    val isFavorite: Boolean,
    val favoriteCount: Int,
    val isFit: Boolean,
)
