package com.draw

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration

@SpringBootApplication
@EnableFeignClients
@ImportAutoConfiguration(classes = [FeignAutoConfiguration::class])
class DrawServerApplication

fun main(args: Array<String>) {
    runApplication<DrawServerApplication>(*args)
}
