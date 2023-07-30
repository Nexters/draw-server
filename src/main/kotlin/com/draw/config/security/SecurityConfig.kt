package com.draw.config.security

import com.draw.component.JwtProvider
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

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
            .securityMatcher("/api/v1/**")
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
