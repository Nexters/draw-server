package com.draw.config

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import feign.codec.Decoder
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
class FeignSnakeCaseDecoder(
    private val messageConverters: ObjectProvider<HttpMessageConverterCustomizer>,
) {
    @Bean
    fun camelCaseDecoder(): Decoder {
        return ResponseEntityDecoder(
            SpringDecoder(
                {
                    HttpMessageConverters(
                        MappingJackson2HttpMessageConverter(
                            jacksonObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE),
                        ),
                    )
                },
                messageConverters,
            ),
        )
    }
}
