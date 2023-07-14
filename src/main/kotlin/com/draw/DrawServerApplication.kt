package com.draw

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DrawServerApplication

fun main(args: Array<String>) {
    runApplication<DrawServerApplication>(*args)
}
