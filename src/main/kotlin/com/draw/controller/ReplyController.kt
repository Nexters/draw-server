package com.draw.controller

import com.draw.common.Const
import com.draw.common.Const.MOCKING
import com.draw.common.Const.MOCK_USER_HEADER
import com.draw.common.Const.REPLY_TAG
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
import com.draw.controller.dto.MyRepliesRes
import com.draw.controller.dto.ReplyCreateReq
import com.draw.controller.dto.ReplyWriterRes
import com.draw.service.ReplyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
@Tag(name = REPLY_TAG, description = "리플 API")
class ReplyController(
    private val replyService: ReplyService,
) {

    @PostMapping("/{feedId}/replies")
    @Operation(summary = "리플 작성")
    fun createReply(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @PathVariable("feedId") feedId: Long,
        @RequestBody replyCreateReq: ReplyCreateReq,
    ) {
        replyService.createReply(userId, feedId, replyCreateReq)
    }

    @GetMapping("/replies/me")
    @Tag(name = Const.MY_TAG, description = "My 관련 API")
    @Operation(summary = "내가 쓴 리플 조회", description = MOCKING)
    fun getMyReplies(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @Parameter(description = "마지막 리플 id") @RequestParam("lastReplyId", required = false) lastReplyId: Long?,
    ): MyRepliesRes {
        return replyService.getMyReplies(userId, lastReplyId)
    }

    @PostMapping("/replies/{replyId}/blocks")
    @Operation(summary = "리플 차단")
    fun blockReply(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @PathVariable("replyId") replyId: Long,
    ) {
        replyService.blockReply(userId, replyId)
    }

    @PostMapping("/replies/{replyId}/claims")
    @Operation(summary = "리플 신고")
    fun claimReply(
        @RequestHeader(MOCK_USER_HEADER) userId: Long,
        @PathVariable("replyId") replyId: Long,
    ) {
        replyService.claimReply(userId, replyId)
    }

    @PostMapping("/replies/{replyId}/peek")
    @Operation(summary = "리플 훔쳐보기", description = MOCKING)
    fun peekReply(
        @PathVariable("replyId") replyId: Long,
    ): ReplyWriterRes {

        // TODO: 여기에서 포인트 제외 & peek 저장 & 관련 서비스로직이 들어가지 않을까 추측 2023/07/24 (koi)
        return ReplyWriterRes(MBTI.ENFP, Gender.MALE)
    }
}
