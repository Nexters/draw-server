package com.draw.config.security

import com.draw.common.enums.ErrorType
import com.draw.common.response.ErrorRes
import com.draw.component.JwtProvider
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class OAuthSecurityFilter(
    private val jwtProvider: JwtProvider,
    private val authorizationRequiredRequestMatcher: DrawApiRequestMatcher,
    private val objectMapper: ObjectMapper,
) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse, chain: FilterChain) {
        (response as HttpServletResponse).setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Credentials", "false")
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD")
        val tokenHeader = (request as HttpServletRequest).getHeader(HttpHeaders.AUTHORIZATION)
        if (request.method == "OPTIONS") {
            writeOptionSuccess(response)
            return
        }
        if (tokenHeader == null && !authorizationRequiredRequestMatcher.matches(request)) {
            chain.doFilter(request, response)
        } else {
            runCatching {
                val token = tokenHeader.toString().removePrefix("Bearer")
                val authentication = jwtProvider.authenticate(token)
                SecurityContextHolder.getContext().authentication = authentication
            }.onFailure { e ->
                when (e) {
                    is ExpiredJwtException -> response.writer.write(
                        objectMapper.writeValueAsString(
                            ErrorRes.of(ErrorType.ACCESS_TOKEN_EXPIRED),
                        ),
                    )
                    else -> writeUnAuthorizedResponse(response)
                }
                return
            }
        }
    }

    private fun writeUnAuthorizedResponse(response: ServletResponse) {
        response as HttpServletResponse
        val writer = response.writer
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        writer.print(objectMapper.writeValueAsString(ErrorRes.of(ErrorType.UNAUTHORIZED)))
        writer.flush()
    }

    private fun writeOptionSuccess(response: ServletResponse) {
        response as HttpServletResponse
        val writer = response.writer
        response.status = 204
        writer.flush()
    }
}
