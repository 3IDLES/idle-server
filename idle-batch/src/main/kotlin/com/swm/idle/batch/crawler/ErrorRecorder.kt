package com.swm.idle.batch.crawler

import io.github.oshai.kotlinlogging.KotlinLogging

class ErrorRecorder {

    private val errorCounts: MutableMap<String, Int> = mutableMapOf()
    private val logger = KotlinLogging.logger {}

    fun recordError(location: String) {
        errorCounts[location] = errorCounts.getOrDefault(location, 0) + 1
    }

    fun printErrors() {
        errorCounts.forEach { (location, count) ->
            println("Error at $location: $count occurrences")
        }
    }
}