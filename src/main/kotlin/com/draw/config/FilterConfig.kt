package com.draw.config

import com.draw.controller.filter.LoggingFilter
import com.draw.controller.filter.MDCFilter
import jakarta.servlet.Filter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun registerMDCFilter(): FilterRegistrationBean<Filter> {
        return FilterRegistrationBean<Filter>().apply {
            filter = MDCFilter()
            order = 1
            urlPatterns = listOf("/api/*")
        }
    }

    @Bean
    fun registerLoggingFilter(): FilterRegistrationBean<Filter> {
        return FilterRegistrationBean<Filter>().apply {
            filter = LoggingFilter()
            order = 2
            urlPatterns = listOf("/api/*")
        }
    }
}
