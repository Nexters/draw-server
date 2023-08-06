package com.draw.config.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpMethod
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component

@Component
class DrawApiRequestMatcher : RequestMatcher {
    private val includedMatcher = AntPathRequestMatcher("/api/v1/**")
    private val excludeMatchers = listOf(
        matcherOf("/api/v1/feeds", HttpMethod.GET),
        matcherOf("/api/v1/feeds/{feedId:[0-9]+}", HttpMethod.GET),
    )

    override fun matches(request: HttpServletRequest?): Boolean {
        return includedMatcher.matches(request) && excludeMatchers.all { !it.matches(request) }
    }

    companion object {
        fun matcherOf(pattern: String, method: HttpMethod): AntPathRequestMatcher {
            return AntPathRequestMatcher(pattern, method.name())
        }
    }
}
