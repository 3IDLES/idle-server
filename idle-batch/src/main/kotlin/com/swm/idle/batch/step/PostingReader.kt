package com.swm.idle.batch.step

import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import com.swm.idle.batch.crawler.CrawlerConsts
import com.swm.idle.batch.crawler.WorknetPageCrawler
import com.swm.idle.batch.crawler.WorknetPostCrawler
import org.springframework.batch.item.ItemStreamReader
import java.util.concurrent.atomic.AtomicInteger

class PostingReader : ItemStreamReader<List<CrawledJobPostingDto>> {
    var crawlingUrl: String = ""
    var postingCount: Int = 1
    var lastPageJobPostingCount: Int = 1
    var pageCount: Int = 1

    init {
        WorknetPageCrawler().initCounts(this)
    }

    companion object {
        var nextPage = AtomicInteger(1)
    }

    override fun read(): List<CrawledJobPostingDto>? {
        val currentPage = nextPage.getAndIncrement()
        if (currentPage > pageCount) return null

        val amount = getAmount(currentPage)
        val nowUrl = getPageUrl(currentPage)

        return WorknetPostCrawler().crawlPosts(amount, nowUrl)
    }

    private fun getAmount(currentPage: Int): Int {
        if (currentPage == pageCount && lastPageJobPostingCount > 0) {
            return lastPageJobPostingCount
        }
        return CrawlerConsts.JOB_POSTING_COUNT_PER_PAGE.getIntValue()
    }

    private fun getPageUrl(currentPage: Int): String {
        return crawlingUrl.replace(
            Regex("pageIndex=\\d+"),
            "pageIndex=$currentPage")
    }
}