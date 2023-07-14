package com.draw.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun apiFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { csrf -> csrf.disable() }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .sessionManagement { mgmt -> mgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                it.requestMatchers("/health", "/ready").permitAll()
                it.requestMatchers("/").permitAll()
                it.anyRequest().authenticated()
            }
            .build()
    }
}
