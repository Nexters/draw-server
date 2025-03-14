package com.draw.controller

import com.draw.common.Const.FEED_TAG
import com.draw.common.Const.MY_TAG
import com.draw.controller.dto.FeedCreateReq
import com.draw.controller.dto.FeedRes
import com.draw.controller.dto.FeedsRes
import com.draw.controller.dto.MyFavoriteFeedsRes
import com.draw.domain.user.User
import com.draw.service.FeedService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    @Operation(summary = "피드 조회")
    fun getFeeds(
        @AuthenticationPrincipal user: User?,
        @Parameter(description = "마지막 피드 id")
        @RequestParam("lastFeedId", required = false)
        lastFeedId: Long?,
    ): FeedsRes {
        return feedService.getFeeds(user, lastFeedId)
    }

    @PostMapping
    @Operation(summary = "피드 작성")
    fun createFeed(
        @AuthenticationPrincipal user: User,
        @RequestBody @Valid feedCreateReq: FeedCreateReq,
    ) {
        feedService.createFeed(user, feedCreateReq)
    }

    @GetMapping("/me")
    @Tag(name = MY_TAG, description = "My 관련 API")
    @Operation(summary = "내가 쓴 피드 조회")
    fun getFeedsByMe(
        @AuthenticationPrincipal user: User,
        @Parameter(description = "마지막 피드 id")
        @RequestParam("lastFeedId", required = false)
        lastFeedId: Long?,
    ): FeedsRes {
        return feedService.getMyFeeds(user, lastFeedId)
    }

    @GetMapping("/me/favorites")
    @Tag(name = MY_TAG, description = "My 관련 API")
    @Operation(summary = "내가 좋아요한 피드 조회")
    fun getFeedsByMeFavorites(
        @AuthenticationPrincipal user: User,
        @Parameter(description = "마지막 좋아요 id")
        @RequestParam("lastFavoriteId", required = false)
        lastFavoriteId: Long?,
    ): MyFavoriteFeedsRes {
        return feedService.getMyFavoriteFeeds(user, lastFavoriteId)
    }

    @GetMapping("/{feedId}")
    @Operation(summary = "피드 조회 (상세)")
    fun getFeed(
        @AuthenticationPrincipal user: User?,
        @Parameter(description = "피드 id")
        @PathVariable("feedId")
        feedId: Long,
    ): FeedRes {
        return feedService.getFeed(user, feedId)
    }

    @PostMapping("/{feedId}/views")
    @Operation(summary = "피드 확인 기록 저장")
    fun createFeedView(
        @AuthenticationPrincipal user: User,
        @Parameter(description = "피드 id")
        @PathVariable("feedId")
        feedId: Long,
    ) {
        feedService.createFeedView(user, feedId)
    }

    @PostMapping("/{feedId}/blocks")
    @Operation(summary = "피드 차단")
    fun blockFeed(
        @AuthenticationPrincipal user: User,
        @Parameter(description = "피드 id")
        @PathVariable("feedId")
        feedId: Long,
    ) {
        feedService.blockFeed(user, feedId)
    }

    @PostMapping("/{feedId}/claims")
    @Operation(summary = "피드 신고")
    fun claimFeed(
        @AuthenticationPrincipal user: User,
        @Parameter(description = "피드 id")
        @PathVariable("feedId")
        feedId: Long,
    ) {
        feedService.claimFeed(user, feedId)
    }

    @PostMapping("/{feedId}/favorites")
    @Operation(
        summary = "피드 좋아요",
        responses = [ApiResponse(responseCode = "400(40002)", description = "FAVORITE_FEED_ALREADY_EXISTS")],
    )
    fun createFavoriteFeed(
        @AuthenticationPrincipal user: User,
        @PathVariable("feedId") feedId: Long,
    ) {
        feedService.createFavoriteFeed(user, feedId)
    }

    @DeleteMapping("/{feedId}/favorites")
    @Operation(summary = "피드 좋아요 취소")
    fun deleteFavoriteFeed(
        @AuthenticationPrincipal user: User,
        @PathVariable("feedId") feedId: Long,
    ) {
        feedService.deleteFavoriteFeed(user, feedId)
    }
}
