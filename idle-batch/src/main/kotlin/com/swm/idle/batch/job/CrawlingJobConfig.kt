package com.swm.idle.batch.job

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class CrawlingJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val crawlingJobPostingTasklet: CrawlingJobPostingTasklet,
) {

    @Bean
    fun crawlingJob(): Job {
        return JobBuilder("crawlingJob", jobRepository)
            .start(crawlingJobPostStep())
            .build()
    }

    @Bean
    fun crawlingJobPostStep(): Step {
        return StepBuilder("crawlingJobPostStep", jobRepository)
            .tasklet(crawlingJobPostingTasklet, transactionManager)
            .allowStartIfComplete(true)
            .build()
    }

}
