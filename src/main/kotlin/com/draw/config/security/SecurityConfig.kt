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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

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
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .sessionManagement { mgmt -> mgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .securityMatcher("/api/v1/**")
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .addFilterBefore(
                OAuthSecurityFilter(jwtProvider, objectMapper),
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
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .sessionManagement { mgmt -> mgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowCredentials = true
            allowedOrigins = listOf("http://localhost:3000", "https://draw-nexters.kro.kr")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type")
            exposedHeaders = listOf("*")
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    private fun permitAllUrls(): Array<String> {
        return arrayOf(
            "/health",
            "/ready",
        )
    }
}
