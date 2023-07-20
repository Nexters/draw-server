package com.draw.controller.filter

import com.draw.common.Const
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

class MDCFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        MDC.put(Const.REQUEST_ID, UUID.randomUUID().toString().substring(0, 8))
        MDC.put(Const.REQUEST_TIME, System.currentTimeMillis().toString())
        MDC.put(Const.USER_ID, "todo") // TODO:  2023/07/19 (koi)

        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }
}
