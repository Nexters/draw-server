package com.draw.config.security

import com.draw.component.JwtProvider
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtProvider: JwtProvider,
    private val objectMapper: ObjectMapper,
) {

    @Bean
    @Order(1)
    fun apiFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { csrf -> csrf.disable() }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .sessionManagement { mgmt -> mgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .securityMatcher(CustomRequestMatcher())
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .addFilterBefore(
                OauthSecurityFilter(jwtProvider, objectMapper),
                BasicAuthenticationFilter::class.java,
            )
            .exceptionHandling { handling ->
                handling.accessDeniedHandler(AuthTokenAccessDeniedHandler(objectMapper))
            }
            .build()
    }

    class CustomRequestMatcher : RequestMatcher {
        private val includedPathMatcher = AntPathRequestMatcher("/api/v1/**")
        private val excludedPathMatcher = AntPathRequestMatcher("/api/v1/oauth/**")

        override fun matches(request: HttpServletRequest): Boolean {
            return includedPathMatcher.matches(request) && !excludedPathMatcher.matches(request)
        }
    }

    @Bean
    @Order(2)
    fun nonBearerAuthorizationFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { csrf -> csrf.disable() }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .sessionManagement { mgmt -> mgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .build()
    }

    private fun permitAllUrls(): Array<String> {
        return arrayOf(
            "/health",
            "/ready",
        )
    }
}
