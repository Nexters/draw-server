package com.draw.controller

import com.draw.common.Const
import com.draw.common.Const.MOCKING
import com.draw.common.Const.REPLY_TAG
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
import com.draw.controller.dto.MyRepliesRes
import com.draw.controller.dto.MyReplyRes
import com.draw.controller.dto.ReplyCreateReq
import com.draw.controller.dto.ReplyWriterRes
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
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
class ReplyController {
    private val log = KotlinLogging.logger { }

    @PostMapping("/feeds/{feedId}/replies")
    @Operation(summary = "리플 작성", description = MOCKING)
    fun createReply(
        @PathVariable("feedId") feedId: Long,
        @RequestBody replyCreateReq: ReplyCreateReq,
    ) {
        // TODO:  2023/07/24 (koi)
    }

    @GetMapping("/replies/me")
    @Tag(name = Const.MY_TAG, description = "My 관련 API")
    @Operation(summary = "내가 쓴 리플 조회", description = MOCKING)
    fun getMyReplies(
        @Parameter(description = "마지막 리플 id") @RequestParam("lastReplyId", required = false) lastReplyId: Long?,
    ): MyRepliesRes {
        // TODO:  2023/07/24 (koi)
        return MyRepliesRes(
            myReplies = listOf(
                MyReplyRes(1, "김치찌개 어때?", 4, "난 반대야"),
                MyReplyRes(2, "나는 T인데 눈물이 없어", 3, "나는 글쓴이야 👋"),
            ),
            hasNext = false
        )
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
