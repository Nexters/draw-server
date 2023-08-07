package com.draw.controller.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class CustomHeaderFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // TODO: cors 테스트 용도 2023/08/08 (koi)
        if (request.method == "OPTIONS") {
            response.setHeader("Access-Control-Allow-Origin", "*")
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, X-Requested-With, remember-me")
            response.setHeader("Access-Control-Max-Age", "3600")

            response.status = HttpServletResponse.SC_OK
            return
        }
        filterChain.doFilter(request, response)
    }
}
