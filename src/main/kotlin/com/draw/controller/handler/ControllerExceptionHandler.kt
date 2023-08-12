package com.draw.controller.handler

import com.draw.common.BusinessException
import com.draw.common.enums.ErrorType
import com.draw.common.response.ErrorRes
import com.draw.infra.external.DiscordApiClient
import com.draw.infra.external.DiscordMessage
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.protocol.Message
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import kotlin.math.min

@ControllerAdvice
class ControllerExceptionHandler(
    private val env: Environment,
    private val discordApiClient: DiscordApiClient,
) {
    private val log = KotlinLogging.logger { }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorRes> {
        val errorMessage = e.bindingResult.fieldErrors.map {
            "field: ${it.field}, value: ${it.rejectedValue}, message: ${it.defaultMessage}"
        }.joinToString { "\n" }

        sendNotification(e, request)
        return ResponseEntity.badRequest().body(
            ErrorRes.of(ErrorType.VALIDATION_FAILED, errorMessage),
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        e: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorRes> {
        sendNotification(e, request)
        return ResponseEntity.badRequest().body(
            ErrorRes.of(ErrorType.BAD_REQUEST, e.message ?: ""),
        )
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(e: IllegalStateException, request: HttpServletRequest): ResponseEntity<ErrorRes> {
        sendNotification(e, request)
        return ResponseEntity.badRequest().body(
            ErrorRes.of(ErrorType.BAD_REQUEST),
        )
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException, request: HttpServletRequest): ResponseEntity<ErrorRes> {
        sendNotification(e, request)
        return ResponseEntity.badRequest().body(
            ErrorRes.of(e.errorType),
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): ResponseEntity<ErrorRes> {
        sendNotification(e, request)
        return ResponseEntity.internalServerError()
            .body(
                ErrorRes.of(ErrorType.UNKNOWN_ERROR),
            )
    }

    private fun sendNotification(e: Exception, request: HttpServletRequest) {
        if (!env.activeProfiles.contains("prod")) {
            e.printStackTrace()
            return
        }

        val contentTemplate = """
                        ${e.javaClass.simpleName}: ${e.message}
                        
                        ${e.stackTraceToString().subSequence(0, 1000)}
                        ${request.method} ${request.requestURI}
                        
                        ${request.parameterMap.map { "${it.key}: ${it.value.joinToString()}" }.joinToString("\n")}
                        """
            .trimIndent()

        val length = min(contentTemplate.length, 3000)
        try {
            discordApiClient.sendMessage(
                DiscordMessage(
                    content = contentTemplate.substring(0, length),
                ),
            )

            Sentry.captureEvent(
                SentryEvent().apply {
                    message = Message().apply {
                        message = """${e.javaClass.simpleName}: ${e.message}"""
                    }
                    level = SentryLevel.ERROR
                    setExtra("requestURI", request.requestURI)
                    setExtra("method", request.method)
                    setExtra("parameterMap", request.parameterMap)
                    setExtra("stackTrace", e.stackTraceToString())
                })
        } catch (e: Exception) {
            log.error("Failed to send slack notification", e)
        }
    }
}
