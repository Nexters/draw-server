package com.draw.controller

import com.draw.common.Const.FEED_TAG
import com.draw.common.Const.MOCKING
import com.draw.common.Const.MOCK_USER_HEADER
import com.draw.common.Const.MY_TAG
import com.draw.controller.dto.FeedCreateReq
import com.draw.controller.dto.FeedRes
import com.draw.controller.dto.FeedsRes
import com.draw.service.FeedService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/feeds")
@Tag(name = FEED_TAG, description = "피드 API")
class FeedController(
    private val feedService: FeedService,
) {

    @GetMapping
    @Operation(summary = "피드 조회", description = MOCKING)
    fun getFeeds(
        @RequestHeader(MOCK_USER_HEADER) userId: Long?,
        @Parameter(description = "마지막 피드 id") @RequestParam("lastFeedId", required = false) lastFeedId: Long?,
    ): FeedsRes {
        return feedService.getFeeds(userId, lastFeedId)
    }

    @PostMapping
    @Operation(summary = "피드 작성")
    fun createFeed(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @RequestBody feedCreateReq: FeedCreateReq
    ) {
        feedService.createFeed(userId, feedCreateReq)
    }

    @GetMapping("/me")
    @Tag(name = MY_TAG, description = "My 관련 API")
    @Operation(summary = "내가 쓴 피드 조회")
    fun getFeedsByMe(
        @RequestHeader(MOCK_USER_HEADER, required = false) userId: Long,
        @Parameter(description = "마지막 피드 id") @RequestParam("lastFeedId", required = false) lastFeedId: Long?,
    ): FeedsRes {
        return feedService.getMyFeeds(userId, lastFeedId)
    }

    @GetMapping("/me/favorites")
    @Tag(name = MY_TAG, description = "My 관련 API")
    @Operation(summary = "내가 좋아요한 피드 조회")
    fun getFeedsByMeFavorites(
        @RequestHeader(MOCK_USER_HEADER, required = false) userId: Long,
        @Parameter(description = "마지막 피드 id") @RequestParam("lastFeedId", required = false) lastFeedId: Long?,
    ): FeedsRes {

        return feedService.getMyFavoriteFeeds(userId, lastFeedId)
    }

    @GetMapping("/{feedId}")
    @Operation(summary = "피드 조회 (상세)", description = MOCKING)
    fun getFeed(
        @RequestHeader(MOCK_USER_HEADER, required = false) userId: Long?,
        @Parameter(description = "피드 id") @PathVariable("feedId") feedId: Long,
    ): FeedRes {
        return feedService.getFeed(userId, feedId)
    }

    @PostMapping("/{feedId}/views")
    @Operation(summary = "피드 확인 기록 저장")
    fun createFeedView(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @Parameter(description = "피드 id") @PathVariable("feedId") feedId: Long,
    ) {
        feedService.createFeedView(userId, feedId)
    }

    @PostMapping("/{feedId}/blocks")
    @Operation(summary = "피드 차단")
    fun blockFeed(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @Parameter(description = "피드 id") @PathVariable("feedId") feedId: Long,
    ) {
        feedService.blockFeed(userId, feedId)
    }

    @PostMapping("/{feedId}/claims")
    @Operation(summary = "피드 신고")
    fun claimFeed(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @Parameter(description = "피드 id") @PathVariable("feedId") feedId: Long,
    ) {
        feedService.claimFeed(userId, feedId)
    }

    @PostMapping("/{feedId}/favorites")
    @Operation(
        summary = "피드 좋아요",
        responses = [ApiResponse(responseCode = "400(40002)", description = "FAVORITE_FEED_ALREADY_EXISTS")]
    )
    fun createFavoriteFeed(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @PathVariable("feedId") feedId: Long,
    ) {
        feedService.createFavoriteFeed(userId, feedId)
    }

    @DeleteMapping("/{feedId}/favorites")
    @Operation(summary = "피드 좋아요 취소")
    fun deleteFavoriteFeed(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @PathVariable("feedId") feedId: Long,
    ) {
        feedService.deleteFavoriteFeed(userId, feedId)
    }
}
