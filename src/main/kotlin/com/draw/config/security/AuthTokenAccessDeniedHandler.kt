package com.draw.config.security

import com.draw.common.enums.ErrorType
import com.draw.common.response.ErrorRes
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler

class AuthTokenAccessDeniedHandler(
    private val objectMapper: ObjectMapper,
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        response.characterEncoding = Charsets.UTF_8.displayName()
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        response.status = HttpStatus.FORBIDDEN.value()
        response.writer.write(
            objectMapper.writeValueAsString(
                ErrorRes.of(ErrorType.UNAUTHORIZED),
            ),
        )
    }
}
