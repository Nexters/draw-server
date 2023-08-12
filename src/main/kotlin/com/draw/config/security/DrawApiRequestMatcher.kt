package com.draw.config.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component

@Component
class DrawApiRequestMatcher : RequestMatcher {
    private val includedMatcher = AntPathRequestMatcher("/api/v1/**")
    private val excludeMatchers = listOf(
        matcherOf("/api/v1/feeds"),
        matcherOf("/api/v1/feeds/{feedId:[0-9]+}"),
    )

    override fun matches(request: HttpServletRequest?): Boolean {
        return includedMatcher.matches(request) && excludeMatchers.all { !it.matches(request) }
    }

    companion object {
        fun matcherOf(pattern: String): AntPathRequestMatcher {
            return AntPathRequestMatcher(pattern)
        }
    }
}
