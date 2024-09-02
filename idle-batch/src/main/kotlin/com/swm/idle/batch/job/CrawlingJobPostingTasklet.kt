package com.swm.idle.batch.job

import com.swm.idle.application.jobposting.domain.CrawlingJobPostingService
import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import com.swm.idle.batch.util.WorknetCrawler
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

@Component
class CrawlingJobPostingTasklet(
    private val crawlingJobPostingService: CrawlingJobPostingService,
) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        WorknetCrawler.run()?.let {
            crawlingJobPostingService.saveAll(
                it.stream()
                    .map(CrawledJobPostingDto::toDomain)
                    .toList()
            )
        }

        return RepeatStatus.FINISHED
    }

}
