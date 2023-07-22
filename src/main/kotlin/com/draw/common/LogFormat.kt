package com.draw.common

import com.draw.common.enums.LogType

data class LogFormat(
    val path: String,
    val method: String,
    val reqParam: String,
    val body: String,
    val status: Int,
    val resPayload: String,
    val logType: LogType,
    val duration: Long,
    val requestId: String,
    val userId: String,
    val time: String,
    val host: String,
)
