package com.swm.idle.batch.crawler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WorknetPageParser(dayOffset: Long = 0) {
    private var postingCount = 0
    private var crawlingUrl: String = ""
    private var lastPageJobPostingCount: Int = 1
    private var pageCount: Int = 1

    private val logger = KotlinLogging.logger { }

    init {
        val driver = DriverInitializer.init()
        driver.safeUse {
            getCrawlingURL(dayOffset)
            moveToPage(driver)
            getPostingCount(driver)
            calculatePageInfo()
            LoggingPageResult()
        }
    }

    fun isOverPage(currentPage: Int): Boolean {
        return currentPage > pageCount
    }

    fun getFetchCount(currentPage: Int): Int {
        if (currentPage == pageCount && lastPageJobPostingCount > 0) {
            return lastPageJobPostingCount
        }
        return CrawlerConsts.JOB_POSTING_COUNT_PER_PAGE.getIntValue()
    }

    /**
     * Generates the URL for accessing a specific page of job postings.
     *
     * Replaces the page index parameter in the base crawling URL with the given page number.
     *
     * @param currentPage The page number to access.
     * @return The URL for the specified page.
     */
    fun getAccessURL(currentPage: Int): String {
        return crawlingUrl.replace(
            Regex("pageIndex=\\d+"),
            "pageIndex=$currentPage")
    }

    /**
     * Constructs the crawling URL by substituting the target date, offset by the specified number of days, and setting the page index to 1.
     *
     * @param dayOffset The number of days to subtract from the current date to determine the target date for the URL.
     */
    private fun getCrawlingURL(dayOffset: Long) {
        val targetDate = LocalDate.now().minusDays(dayOffset)
        crawlingUrl = CrawlerConsts.CRAWLING_TARGET_URL_FORMAT.value
            .replace("{yesterday}", targetDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            .replace("{pageIndex}", "1")
    }

    private fun moveToPage(driver: WebDriver) {
        driver.get(crawlingUrl)
        WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(
                    CrawlerConsts.JOB_POSTING_COUNT.value
                )))
    }

    private fun getPostingCount(driver: WebDriver) {
        postingCount = driver
            .findElement(By.xpath(CrawlerConsts.JOB_POSTING_COUNT.value))
            .text
            .replace(",", "")
            .toInt()
            .takeIf { it > 0 }
            ?: run {
                throw Exception("크롤링 할 공고가 없습니다.")
            }
    }

    private fun calculatePageInfo() {
        pageCount =
            (postingCount + CrawlerConsts.JOB_POSTING_COUNT_PER_PAGE.getIntValue() - 1) /
                    CrawlerConsts.JOB_POSTING_COUNT_PER_PAGE.getIntValue()
        lastPageJobPostingCount =
            postingCount % CrawlerConsts.JOB_POSTING_COUNT_PER_PAGE.getIntValue()
    }

    private fun LoggingPageResult() {
        logger.info { "PageCount : ${pageCount}" }
        logger.info { "PostingCount : ${postingCount}" }
        logger.info { "LastPage JobCount : ${lastPageJobPostingCount}" }
    }

    private fun <T> WebDriver.safeUse(block: (WebDriver) -> T): T {
        try {
            return block(this)
        } finally {
            this.quit()
        }
    }
}
