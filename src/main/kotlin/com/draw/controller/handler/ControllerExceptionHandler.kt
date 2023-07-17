package com.draw.controller.handler

import com.draw.common.BusinessException
import com.draw.common.enums.ErrorType
import com.draw.common.response.ErrorRes
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorRes> {
        val errorMessage = e.bindingResult.fieldErrors.map {
            "field: ${it.field}, value: ${it.rejectedValue}, message: ${it.defaultMessage}"
        }.joinToString { "\n" }

        return ResponseEntity.badRequest().body(
            ErrorRes.of(ErrorType.VALIDATION_FAILED, errorMessage)
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorRes> {
        return ResponseEntity.badRequest().body(
            ErrorRes.of(ErrorType.BAD_REQUEST)
        )
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(e: IllegalStateException): ResponseEntity<ErrorRes> {
        return ResponseEntity.badRequest().body(
            ErrorRes.of(ErrorType.BAD_REQUEST)
        )
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorRes> {
        return ResponseEntity.badRequest().body(
            ErrorRes.of(e.errorType)
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorRes> {
        return ResponseEntity.internalServerError()
            .body(
                ErrorRes.of(ErrorType.UNKNOWN_ERROR)
            )
    }
}
