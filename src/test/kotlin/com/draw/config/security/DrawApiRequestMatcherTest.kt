package com.draw.config.security

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpMethod
import org.springframework.mock.web.MockHttpServletRequest
import java.util.stream.Stream

class DrawApiRequestMatcherTest {

    private val sut = DrawApiRequestMatcher()

    @ParameterizedTest
    @MethodSource("excludedRequests")
    fun `인증이 필요없는 요청들에 대해서는 false 를 반환한다`(request: Request) {
        val servletRequest = MockHttpServletRequest(request.method.name(), request.url)
        servletRequest.servletPath = request.url
        assertFalse(sut.matches(servletRequest))
    }

    @ParameterizedTest
    @MethodSource("includedRequests")
    fun `인증이 적용되어야하는 요청들에 대해서는 true 를 반환한다`(request: Request) {
        val servletRequest = MockHttpServletRequest(request.method.name(), request.url)
        servletRequest.servletPath = request.url
        assertTrue(sut.matches(servletRequest))
    }

    data class Request(val url: String, val method: HttpMethod)

    companion object {
        @JvmStatic
        fun includedRequests(): Stream<Request> {
            return Stream.of(
                Request("/api/v1/feeds/me/favorites", HttpMethod.GET),
                Request("/api/v1/feeds/me", HttpMethod.GET),
            )
        }

        @JvmStatic
        fun excludedRequests(): Stream<Request> {
            return Stream.of(
                Request("/api/v1/feeds", HttpMethod.GET),
                Request("/api/v1/feeds/1", HttpMethod.GET),
            )
        }
    }
}
