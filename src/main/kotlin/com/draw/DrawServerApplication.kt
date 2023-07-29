package com.draw

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration

@SpringBootApplication(
    exclude = [ErrorMvcAutoConfiguration::class]
)
@EnableFeignClients
@ImportAutoConfiguration(classes = [FeignAutoConfiguration::class])
@ConfigurationPropertiesScan
class DrawServerApplication

fun main(args: Array<String>) {
    runApplication<DrawServerApplication>(*args)
}
