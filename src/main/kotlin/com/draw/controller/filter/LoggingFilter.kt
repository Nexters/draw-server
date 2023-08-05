package com.draw.controller.filter

import com.draw.common.Const
import com.draw.utils.LogUtils
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.http.HttpMethod
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import kotlin.math.min

class LoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val contentCachingRequestWrapper = ContentCachingRequestWrapper(request)
        val contentCachingResponseWrapper = ContentCachingResponseWrapper(response)
        try {
            filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper)
        } finally {
            printLog(contentCachingRequestWrapper, contentCachingResponseWrapper)
            contentCachingResponseWrapper.copyBodyToResponse()
        }
    }

    private fun printLog(request: HttpServletRequest, response: HttpServletResponse) {
        val duration = System.currentTimeMillis() - MDC.get(Const.REQUEST_TIME).toLong()
        if (duration > 1000) {
            log.warn { "${request.requestURI} response is slow: $duration" }
        }

        val params = mapper.writeValueAsString(request.parameterMap)
        val body = if (request.hasBodyMethod()) {
            request.bodyToString()
        } else {
            ""
        }

        val responseArray = (response as ContentCachingResponseWrapper).contentAsByteArray
        val responseStr = String(responseArray, charset(Charsets.UTF_8.name()))

        if (!loggingExcludePaths.contains(request.pathInfo)) {
            LogUtils.accessLog(
                path = request.requestURI,
                method = request.method,
                reqParam = params,
                body = body,
                status = response.status,
                resPayload = responseStr,
                duration = duration,
                host = request.remoteHost,
            )
        }
    }

    private fun HttpServletRequest.bodyToString(): String {
        val buf = (this as ContentCachingRequestWrapper).contentAsByteArray

        if (buf.isEmpty()) {
            return ""
        }

        val length = min(buf.size, LOGGING_BODY_MAX_LENGTH)
        return String(buf, 0, length, charset(this.characterEncoding))
    }

    private fun HttpServletRequest.hasBodyMethod(): Boolean {
        return listOf(HttpMethod.POST.name(), HttpMethod.PUT.name()).contains(this.method)
    }

    companion object {
        private const val LOGGING_BODY_MAX_LENGTH = 1024

        private val log = KotlinLogging.logger { }
        private val mapper = ObjectMapper()
        private val loggingExcludePaths = listOf("/actuator")
    }
}
