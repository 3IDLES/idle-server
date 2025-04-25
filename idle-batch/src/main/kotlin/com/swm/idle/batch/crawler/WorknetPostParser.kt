package com.swm.idle.batch.crawler

import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.openqa.selenium.WebElement

class WorknetPostParser {
    private val errorRecorder = ErrorRecorder()

    fun crawlPosts(end: Int, url: String): List<CrawledJobPostingDto> {
        val result = mutableListOf<CrawledJobPostingDto>()
        val driver = DriverInitializer.init()

        driver.safeUse {
            moveToPage(driver, url)

            for (index in 1..end) {
                val originalWindow = driver.windowHandle
                val titleElement = findElementOrNull(driver, By.xpath("//*[@id=\"list${index}\"]/td[1]/div/div[2]/a"))
                    ?: continue

                moveToPostDetailWindow(driver, titleElement, originalWindow)
                putPostsToResult(driver, result)
                backWindow(driver, originalWindow)
            }
        }

        errorRecorder.printErrors()
        return result
    }

    private fun <T> WebDriver.safeUse(block: (WebDriver) -> T): T {
        try {
            return block(this)
        } finally {
            this.quit()
        }
    }

    private fun findElementOrNull(driver: WebDriver, by: By): WebElement? {
        return runCatching { driver.findElement(by) }.getOrNull()
    }

    private fun moveToPage(driver: WebDriver, url: String) {
        driver.get(url)
        WebDriverWait(driver, Duration.ofSeconds(10))
            .until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#list1"))
            )
    }

    private fun moveToPostDetailWindow(driver: WebDriver, titleElement: WebElement, originalWindow: String) {
        titleElement.click()
        WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.numberOfWindowsToBe(2))
        driver.switchTo().window(driver.windowHandles.first { it != originalWindow })
    }

    private fun putPostsToResult(driver: WebDriver, result: MutableList<CrawledJobPostingDto> ) {
        runCatching {
            CrawledJobPostingDto(
                title = extractText(driver,CrawlerConsts.TITLE),
                content = extractText(driver, CrawlerConsts.CONTENT),
                createdAt = extractText(driver, CrawlerConsts.CREATED_AT),
                payInfo = extractText(driver, CrawlerConsts.PAY_INFO),
                workSchedule = extractText(driver, CrawlerConsts.WORK_SCHEDULE),
                recruitmentProcess = extractText(driver, CrawlerConsts.RECRUITMENT_PROCESS),
                applyMethod = extractText(driver, CrawlerConsts.APPLY_METHOD),
                requiredDocument = extractText(driver, CrawlerConsts.REQUIRED_DOCUMENT),
                centerName = extractText(driver, CrawlerConsts.CENTER_NAME),
                applyDeadline = extractApplyDeadline(driver, CrawlerConsts.APPLY_DEADLINE),
                workTime = extractWorkTime(driver, CrawlerConsts.WORK_TIME),
                centerAddress = extractAddress(driver,
                    CrawlerConsts.CLIENT_ADDRESS1,
                    CrawlerConsts.CLIENT_ADDRESS2
                ),
                clientAddress = extractAddress(driver,
                    CrawlerConsts.CENTER_ADDRESS1,
                    CrawlerConsts.CENTER_ADDRESS2,
                    CrawlerConsts.CENTER_ADDRESS3
                ),
                directUrl = driver.currentUrl
            )
        }.onSuccess { post -> result.add(post) }
    }

    private fun backWindow(driver:WebDriver, originalWindow: String?) {
        driver.close()
        driver.switchTo().window(originalWindow)
    }

    private fun extractText(driver: WebDriver, con: CrawlerConsts): String {
        return runCatching {
            driver.findElement(By.xpath(con.value)).text
        }.getOrElse { e->
            errorRecorder.recordError(con.location)
            throw e
        }
    }

    private fun extractApplyDeadline(driver: WebDriver, con: CrawlerConsts): String {
        return runCatching {
            driver.findElement(By.xpath(con.value)).text.let {
                if (it.contains("채용시까지"))
                    LocalDate.now().plusDays(15).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                else
                    it
            }
        }.getOrElse { e->
            errorRecorder.recordError(con.location)
            throw e
        }
    }

    private fun extractAddress(driver: WebDriver,vararg cons: CrawlerConsts): String {
        runCatching {
            for (con in cons) {
                val address = driver.findElement(By.xpath(con.value)).text
                return address.replace("지도보기", "").trim().replace(Regex("\\(\\d{5}\\)"), "").trim()
            }
            throw NoSuchElementException("Center address not found using any of the provided XPaths")
        }.getOrElse { e->
            errorRecorder.recordError(cons[0].location)
            throw e
        }
    }

    private fun extractWorkTime(driver: WebDriver, con: CrawlerConsts): String {
        return runCatching {
            driver.findElement(By.xpath(con.value)).text
                .replace("도움말", "")
                .replace("(근무시간)", "")
                .replace("\n", "")
        }.getOrElse { e->
            errorRecorder.recordError(con.location)
            throw e
        }
    }
}