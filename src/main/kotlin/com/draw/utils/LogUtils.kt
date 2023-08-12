package com.draw.utils

import com.draw.common.Const
import com.draw.common.LogFormat
import com.draw.common.enums.LogType
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.slf4j.MDC
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object LogUtils {

    private val log = KotlinLogging.logger { }
    private val mapper = ObjectMapper()
    fun accessLog(
        path: String,
        method: String,
        reqParam: String,
        body: String,
        status: Int,
        resPayload: String,
        duration: Long,
        host: String,
    ) {

        val format = LogFormat(
            path = path,
            method = method,
            reqParam = reqParam,
            body = body,
            status = status,
            resPayload = resPayload,
            logType = LogType.ACCESS,
            duration = duration,
            requestId = MDC.get(Const.REQUEST_ID),
            time = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            host = host,
        )

        log.info { mapper.writeValueAsString(format).trimIndent() }
    }
}
