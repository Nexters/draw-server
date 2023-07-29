package com.draw.controller

import com.draw.common.Const.FEED_TAG
import com.draw.common.Const.MOCKING
import com.draw.common.Const.MOCK_USER_HEADER
import com.draw.common.Const.MY_TAG
import com.draw.common.Const.REPLY_TAG
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
import com.draw.controller.dto.FeedCreateReq
import com.draw.controller.dto.FeedDetailRes
import com.draw.controller.dto.FeedRes
import com.draw.controller.dto.FeedsRes
import com.draw.controller.dto.ReplyCreateReq
import com.draw.controller.dto.ReplyRes
import com.draw.controller.dto.ReplyStatus
import com.draw.controller.dto.ReplyWriterRes
import com.draw.service.FeedService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
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
    @Operation(summary = "피드 작성")
    fun createFeed(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @RequestBody feedCreateReq: FeedCreateReq
    ) {
        feedService.createFeed(userId, feedCreateReq)
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

    @GetMapping("/{feedId}")
    @Operation(summary = "피드 조회 (상세)", description = MOCKING)
    fun getFeed(
        @Parameter(description = "피드 id") @PathVariable("feedId") feedId: Long,
    ): FeedDetailRes {

        // TODO:  2023/07/27 (koi)
        return FeedDetailRes(
            id = 1,
            content = "저녁먹을사람!",
            isFavorite = true,
            favoriteCount = 1,
            isFit = true,
            replies = listOf(
                ReplyRes(1, "김치찌개 어때?", ReplyStatus.NORMAL, 4L, null),
                ReplyRes(2, "난 반대야", ReplyStatus.PEEKED, 2L, ReplyWriterRes(MBTI.ENFP, Gender.FEMALE)),
                ReplyRes(3, "나는 T인데 눈물이 없어", ReplyStatus.PEEKED, 3L, ReplyWriterRes(MBTI.ESTJ, Gender.MALE)),
                ReplyRes(4, "나는 글쓴이야 😁", ReplyStatus.MINE, 1L, null),
            )
        )
    }

    @PostMapping("/{feedId}/view")
    @Operation(summary = "피드 확인 기록 저장")
    fun createFeedView(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @Parameter(description = "피드 id") @PathVariable("feedId") feedId: Long,
    ) {
        feedService.createFeedView(userId, feedId)
    }

    @PostMapping("/{feedId}/replies")
    @Tag(name = REPLY_TAG)
    @Operation(summary = "리플 작성")
    fun createReply(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @PathVariable("feedId") feedId: Long,
        @RequestBody replyCreateReq: ReplyCreateReq,
    ) {
        feedService.createReply(userId, feedId, replyCreateReq)
    }

    @PostMapping("/{feedId}/favorites")
    @Operation(
        summary = "피드 좋아요",
        responses = [ApiResponse(responseCode = "400(4002)", description = "FAVORITE_FEED_ALREADY_EXISTS")]
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
