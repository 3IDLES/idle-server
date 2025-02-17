package com.swm.idle.batch.crawler

import com.swm.idle.batch.step.PostingReader
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WorknetPageCrawler {
    private var driver: WebDriver = DriverInitializer.init()

    fun initCounts(reader: PostingReader) {
        reader.crawlingUrl = CrawlerConsts.CRAWLING_TARGET_URL_FORMAT.value
                .replace("{yesterday}", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .replace("{pageIndex}", "1")

        moveToPage(reader)

        reader.postingCount = driver
            .findElement(By.xpath(CrawlerConsts.JOB_POSTING_COUNT.value))
            .text
            .replace(",", "")
            .toInt()
            .takeIf { it > 0 }
            ?: run {
                driver.quit()
                throw Exception("크롤링 할 공고가 없습니다.")
            }

        reader.pageCount = (reader.postingCount + CrawlerConsts.JOB_POSTING_COUNT_PER_PAGE.getIntValue() - 1) /
                CrawlerConsts.JOB_POSTING_COUNT_PER_PAGE.getIntValue()
        reader.lastPageJobPostingCount = reader.postingCount % CrawlerConsts.JOB_POSTING_COUNT_PER_PAGE.getIntValue()
        driver.quit()
    }

    private fun moveToPage(reader: PostingReader) {
        driver.get(reader.crawlingUrl)
        WebDriverWait(driver, Duration.ofSeconds(10))
            .also {
                it.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CrawlerConsts.JOB_POSTING_COUNT.value)))
            }
    }
}