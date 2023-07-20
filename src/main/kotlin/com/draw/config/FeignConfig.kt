package com.draw.config

import feign.Logger.Level
import feign.codec.ErrorDecoder.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig {

    @Bean
    fun feignLoggerLevel(): Level {
        return Level.FULL
    }

    @Bean
    fun feignErrorDecoder(): feign.codec.ErrorDecoder {
        return Default()
    }
}
