package com.draw.controller.dto

import jakarta.validation.constraints.NotBlank

data class ReplyCreateReq(
    @field:NotBlank(message = "content is required")
    val content: String,
)
