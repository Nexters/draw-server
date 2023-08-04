package com.draw.controller

import com.draw.common.Const
import com.draw.common.Const.REPLY_TAG
import com.draw.controller.dto.MyRepliesRes
import com.draw.controller.dto.RepliesRes
import com.draw.controller.dto.ReplyCreateReq
import com.draw.controller.dto.ReplyWriterRes
import com.draw.domain.user.User
import com.draw.service.ReplyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
@Tag(name = REPLY_TAG, description = "리플 API")
class ReplyController(
    private val replyService: ReplyService,
) {

    @GetMapping("/feeds/{feedId}/replies")
    @Operation(summary = "리플 조회")
    fun getReplies(
        @AuthenticationPrincipal user: User?,
        @PathVariable("feedId") feedId: Long,
    ): RepliesRes {
        return replyService.getReplies(user, feedId)
    }

    @PostMapping("/{feedId}/replies")
    @Operation(summary = "리플 작성")
    fun createReply(
        @AuthenticationPrincipal user: User,
        @PathVariable("feedId") feedId: Long,
        @RequestBody replyCreateReq: ReplyCreateReq,
    ) {
        replyService.createReply(user, feedId, replyCreateReq)
    }

    @GetMapping("/replies/me")
    @Tag(name = Const.MY_TAG, description = "My 관련 API")
    @Operation(summary = "내가 쓴 리플 조회")
    fun getMyReplies(
        @AuthenticationPrincipal user: User,
        @Parameter(description = "마지막 리플 id") @RequestParam("lastReplyId", required = false) lastReplyId: Long?,
    ): MyRepliesRes {
        return replyService.getMyReplies(user, lastReplyId)
    }

    @PostMapping("/replies/{replyId}/blocks")
    @Operation(summary = "리플 차단")
    fun blockReply(
        @AuthenticationPrincipal user: User,
        @PathVariable("replyId") replyId: Long,
    ) {
        replyService.blockReply(user, replyId)
    }

    @PostMapping("/replies/{replyId}/claims")
    @Operation(summary = "리플 신고")
    fun claimReply(
        @AuthenticationPrincipal user: User,
        @PathVariable("replyId") replyId: Long,
    ) {
        replyService.claimReply(user, replyId)
    }

    @PostMapping("/replies/{replyId}/peek")
    @Operation(summary = "리플 훔쳐보기")
    fun peekReply(
        @AuthenticationPrincipal user: User,
        @PathVariable("replyId") replyId: Long,
    ): ReplyWriterRes {
        return replyService.peekReply(user, replyId)
    }
}
