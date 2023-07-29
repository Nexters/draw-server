package com.draw.config.security

import com.draw.component.JwtProvider
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
    fun apiFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { csrf -> csrf.disable() }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .sessionManagement { mgmt -> mgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                it.requestMatchers(*permitAllUrls()).permitAll()
                it.requestMatchers("/").permitAll()
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

    private fun permitAllUrls(): Array<String> {
        return arrayOf(
            "/health",
            "/ready",
            "/api/v1/oauth/**",
        )
    }
}
