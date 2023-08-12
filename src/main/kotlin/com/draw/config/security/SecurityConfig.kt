package com.draw.config.security

import com.draw.service.oauth.UserAuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.GenericFilterBean

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authorizationRequiredRequestMatcher: DrawApiRequestMatcher,
    private val userAuthenticationService: UserAuthenticationService,
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
            .securityMatcher("/api/v1/**")
            .addFilterBefore(
                OAuthSecurityFilter(authorizationRequiredRequestMatcher, userAuthenticationService, objectMapper),
                BasicAuthenticationFilter::class.java,
            )
            .exceptionHandling { handling ->
                handling.accessDeniedHandler(AuthTokenAccessDeniedHandler(objectMapper))
            }
            .build()
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
            .addFilterBefore(
                object : GenericFilterBean() {
                    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
                        (response as HttpServletResponse).setHeader("Access-Control-Allow-Origin", "*")
                        response.setHeader("Access-Control-Allow-Credentials", "false")
                        response.setHeader(
                            "Access-Control-Allow-Methods",
                            "GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD",
                        )
                        response.setHeader(
                            "Access-Control-Allow-Headers",
                            "*",

                        )
                        chain.doFilter(request, response)
                    }
                },
                BasicAuthenticationFilter::class.java,
            )
            .build()
    }
}
