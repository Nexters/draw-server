package com.draw.controller

import com.draw.common.Const.FEED_TAG
import com.draw.common.Const.MOCKING
import com.draw.common.Const.MY_TAG
import com.draw.controller.dto.FeedCreateReq
import com.draw.controller.dto.FeedRes
import com.draw.controller.dto.FeedsRes
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
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
class FeedController {
    private val log = KotlinLogging.logger { }

    @GetMapping
    @Operation(summary = "피드 조회", description = MOCKING)
    fun getFeeds(
        @Parameter(description = "마지막 피드 id") @RequestParam("lastFeedId", required = false) lastFeedId: Long?,
    ): FeedsRes {

        // TODO: 2023/07/24 (koi)
        return FeedsRes(
            feeds = listOf(
                FeedRes(1, "저녁먹을사람!", true, 1, true),
                FeedRes(2, "t도 박은빈 시상식때", false, 1, true),
            ),
            hasNext = false
        )
    }

    @PostMapping
    @Operation(summary = "피드 작성", description = MOCKING)
    fun createFeed(
        @RequestBody feedCreateReq: FeedCreateReq
    ) {

        // TODO:  2023/07/24 (koi)
    }

    @GetMapping("/me")
    @Tag(name = MY_TAG, description = "My 관련 API")
    @Operation(summary = "내가 쓴 피드 조회", description = MOCKING)
    fun getFeedsByMe(
        @Parameter(description = "마지막 피드 id") @RequestParam("lastFeedId", required = false) lastFeedId: Long?,
    ): FeedsRes {

        // TODO: 2023/07/24 (koi)
        return FeedsRes(
            feeds = listOf(
                FeedRes(1, "저녁먹을사람!", true, 1, false),
                FeedRes(2, "t도 박은빈 시상식때", false, 1, false),
            ),
            hasNext = false
        )
    }

    @GetMapping("/me/favorites")
    @Tag(name = MY_TAG, description = "My 관련 API")
    @Operation(summary = "내가 좋아요한 피드 조회", description = MOCKING)
    fun getFeedsByMeFavorites(
        @Parameter(description = "마지막 피드 id") @RequestParam("lastFeedId", required = false) lastFeedId: Long?,
    ): FeedsRes {

        // TODO: 2023/07/24 (koi)
        return FeedsRes(
            feeds = listOf(
                FeedRes(1, "저녁먹을사람!", true, 1, false),
                FeedRes(2, "t도 박은빈 시상식때", false, 1, false),
            ),
            hasNext = false
        )
    }

    @PostMapping("/{feedId}/view")
    @Operation(summary = "피드 조회 (기록)", description = MOCKING)
    fun createFeedView(
        @Parameter(description = "피드 id") @PathVariable("feedId") feedId: Long,
    ) {
        // TODO:  2023/07/24 (koi)
    }

    @PostMapping("/{feedId}/favorites")
    @Operation(summary = "피드 좋아요", description = MOCKING)
    fun createFavoriteFeed(
        @PathVariable("feedId") feedId: Long,
    ) {
        // TODO:  2023/07/24 (koi)
    }

    @DeleteMapping("/{feedId}/favorites")
    @Operation(summary = "피드 좋아요 취소", description = MOCKING)
    fun deleteFavoriteFeed(
        @PathVariable("feedId") feedId: Long,
    ) {
        // TODO:  2023/07/24 (koi)
    }
}
