package com.draw.controller.interceptor

import com.draw.common.enums.ErrorType
import com.draw.common.response.ErrorRes
import com.draw.domain.user.User
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.HandlerInterceptor

/**
 * 요청 처리전 공통적으로 유저의 상태를 검증한다 (ex. 가입완료여부, 밴여부)
 */
class UserValidationInterceptor(
    private val objectMapper: ObjectMapper,
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val writer = response.writer
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        if (authentication != null && authentication.isAuthenticated) {
            if (authentication is AnonymousAuthenticationToken) {
                return true
            }

            val user = authentication.principal as User
            if (!user.registrationCompleted) {
                response.status = HttpStatus.FORBIDDEN.value()
                writer.print(objectMapper.writeValueAsString(ErrorRes.of(ErrorType.INCOMPLETE_REGISTRATION)))
                writer.flush()
                return false
            }
        } else {
            response.status = HttpStatus.UNAUTHORIZED.value()
            writer.print(objectMapper.writeValueAsString(ErrorRes.of(ErrorType.UNAUTHORIZED)))
            writer.flush()
            return false
        }
        return true
    }
}
