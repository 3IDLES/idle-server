package com.swm.idle

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IdleServerApplication

fun main(args: Array<String>) {
    runApplication<IdleServerApplication>(*args)
}
