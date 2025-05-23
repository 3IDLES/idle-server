package com.swm.idle.batch.step

import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import com.swm.idle.batch.crawler.WorknetPageParser
import com.swm.idle.batch.crawler.WorknetPostParser
import org.springframework.batch.item.ItemStreamReader
import java.util.concurrent.atomic.AtomicInteger

class PostingReader(
    dayOffset: Long = 0
) : ItemStreamReader<List<CrawledJobPostingDto>> {

    val pageParser: WorknetPageParser = WorknetPageParser(dayOffset)

    companion object {
        var pageIndex = AtomicInteger(1)
    }

    override fun read(): List<CrawledJobPostingDto>? {
        val accessIndex = pageIndex.getAndIncrement()

        if (pageParser.isOverPage(accessIndex)) {
            pageIndex = AtomicInteger(1)
            return null
        }

        val fetchCount = pageParser.getFetchCount(accessIndex)
        val accessURL = pageParser.getAccessURL(accessIndex)
        val posts = WorknetPostParser().crawlPosts(fetchCount, accessURL)

        return posts
    }
}
