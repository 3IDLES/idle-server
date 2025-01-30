package com.swm.idle.batch.crawler

import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.openqa.selenium.WebElement

class WorknetPostCrawler {
    private val logger = KotlinLogging.logger { }
    private var driver: WebDriver = DriverInitializer.init()
    private var errorCountMap: MutableMap<String, Int> = mutableMapOf()

    fun crawlPosts(end: Int, url: String): List<CrawledJobPostingDto> {
        moveToPage(url)

        val crawledPostings = mutableListOf<CrawledJobPostingDto>()
        repeat(end) { i ->
            val originalWindow = driver.windowHandle
            val titleElement = findElementSafe(By.xpath("//*[@id=\"list${i+1}\"]/td[1]/div/div[2]/a")) ?:  return@repeat

            moveToPostDetailWindow(titleElement, originalWindow)

            try {
                val post: CrawledJobPostingDto = createPost()
                crawledPostings.add(post)
            } catch (e: Exception) {
                logger.warn { "실패" }
            }

            backWindow(originalWindow)
        }
        errorCountMap.asSequence().forEach { (key, value) -> println("$key -> $value") }
        driver.quit()
        return crawledPostings
    }

    private fun moveToPage(url: String) {
        driver.get(url)
        WebDriverWait(driver, Duration.ofSeconds(10))
            .until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#list1"))
            )
    }

    private fun createPost(): CrawledJobPostingDto {
        return CrawledJobPostingDto(
            title = extractText(CrawlerConsts.TITLE),
            content = extractText(CrawlerConsts.CONTENT),
            createdAt = extractText(CrawlerConsts.CREATED_AT),
            payInfo = extractText(CrawlerConsts.PAY_INFO),
            workSchedule = extractText(CrawlerConsts.WORK_SCHEDULE),
            recruitmentProcess = extractText(CrawlerConsts.RECRUITMENT_PROCESS),
            applyMethod = extractText(CrawlerConsts.APPLY_METHOD),
            requiredDocument = extractText(CrawlerConsts.REQUIRED_DOCUMENT),
            centerName = extractText(CrawlerConsts.CENTER_NAME),
            applyDeadline = extractApplyDeadline(CrawlerConsts.APPLY_DEADLINE),
            workTime = extractWorkTime(CrawlerConsts.WORK_TIME),
            centerAddress = extractAddress(
                CrawlerConsts.CLIENT_ADDRESS1,
                CrawlerConsts.CLIENT_ADDRESS2
            ),
            clientAddress = extractAddress(
                CrawlerConsts.CENTER_ADDRESS1,
                CrawlerConsts.CENTER_ADDRESS2,
                CrawlerConsts.CENTER_ADDRESS3
            ),
            directUrl = driver.currentUrl
        )
    }


    private inline fun <T> errorRecord(location: String, action: () -> T): T {
        return runCatching { action() }
            .getOrElse { e ->
                logError(location)
                throw e
            }
    }

    private fun findElementSafe(by: By): WebElement? {
        return runCatching { driver.findElement(by) }.getOrNull()
    }

    private fun moveToPostDetailWindow(titleElement: WebElement, originalWindow: String) {
        titleElement.click()
        WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.numberOfWindowsToBe(2))
        driver.switchTo().window(driver.windowHandles.first { it != originalWindow })
    }

    private fun extractText(con: CrawlerConsts): String {
        return errorRecord(con.location) { driver.findElement(By.xpath(con.value)).text }
    }

    private fun extractApplyDeadline(con: CrawlerConsts): String {
        return errorRecord(con.location) {
            driver.findElement(By.xpath(con.value)).text.let {
                if (it.contains("채용시까지"))
                    LocalDate.now().plusDays(15).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                else
                    it
            }
        }
    }

    private fun extractAddress(vararg cons: CrawlerConsts): String {
        for (con in cons) {
            runCatching {
                val address = driver.findElement(By.xpath(con.value)).text
                return address.replace("지도보기", "").trim().replace(Regex("\\(\\d{5}\\)"), "").trim()
            } .getOrElse { e ->
                logError(con.location)
                throw e
            }
        }
        throw NoSuchElementException("Center address not found using any of the provided XPaths")
    }

    private fun extractWorkTime(con: CrawlerConsts): String {
        return errorRecord(con.location) {
            driver.findElement(By.xpath(con.value)).text
                .replace("도움말", "")
                .replace("(근무시간)", "")
                .replace("\n", "")
        }
    }

    private fun logError(location: String) {
        errorCountMap[location] = errorCountMap.getOrDefault(location, 0) + 1
    }

    private fun backWindow(originalWindow: String?) {
        driver.close()
        driver.switchTo().window(originalWindow)
    }
}