package com.draw.config

import com.draw.controller.interceptor.UserValidationInterceptor
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val objectMapper: ObjectMapper
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(UserValidationInterceptor(objectMapper))
            .addPathPatterns("/api/v1/**")
            .excludePathPatterns("/health", "/ready", "/api/v1/oauth/**")
    }
}
