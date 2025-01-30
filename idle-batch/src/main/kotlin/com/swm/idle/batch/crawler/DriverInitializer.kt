package com.swm.idle.batch.crawler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File

object DriverInitializer {
    private val logger = KotlinLogging.logger { }

    fun init(): ChromeDriver {
        return runCatching {
            ChromeDriver(
                ChromeDriverService.Builder()
                    .usingDriverExecutable(File(System.getenv("CHROMEDRIVER_BIN")))
                    .build()
                    .also { logger.info { System.getenv("CHROMEDRIVER_BIN") } },
                ChromeOptions().apply {
                    addArguments(*CrawlerConsts.getChromOptions())
                    setBinary(System.getenv("CHROME_BIN"))
                }.also { logger.info { System.getenv("CHROME_BIN")} }
            )
        }.getOrElse {
            logger.error { "ChromeDriver initialization failed: ${it.message}" }
            throw RuntimeException("ChromeDriver initialization failed, application will exit.") // 이후 코드가 실행되지 않도록 예외 던짐
        }
    }
}