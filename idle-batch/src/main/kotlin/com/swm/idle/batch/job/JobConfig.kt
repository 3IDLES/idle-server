package com.swm.idle.batch.job

import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import com.swm.idle.batch.step.PostingProcessor
import com.swm.idle.batch.step.PostingReader
import com.swm.idle.batch.step.PostingWriter
import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import org.springframework.beans.factory.annotation.Value
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.Job
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableBatchProcessing
class JobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val redisTemplate: RedisTemplate<String, String>
) {

    @Bean
    fun crawlingJob(crawlStep: Step): Job {
        return JobBuilder("crawlingJob", jobRepository)
            .start(crawlStep)
            .preventRestart()
            .build()
    }

    @Bean
    fun crawlStep(
        postingReader: ItemReader<out List<CrawledJobPostingDto>>,
        postingWriter: ItemWriter<List<CrawledJobPosting>>
    ): Step {
        return StepBuilder("crawlStep", jobRepository)
            .chunk<List<CrawledJobPostingDto>, List<CrawledJobPosting>>(1, transactionManager)
            .reader(postingReader)
            .processor(postingProcessor())
            .writer(postingWriter)
            .taskExecutor(taskExecutor())
            .allowStartIfComplete(true)
            .build()
    }

    @Bean
    fun taskExecutor(): SimpleAsyncTaskExecutor {
        return SimpleAsyncTaskExecutor().apply {
            this.concurrencyLimit = 4
        }
    }

    @Bean
    @StepScope
    fun postingReader(
        @Value("#{jobParameters['day']}") dayParam: Long?
    ): ItemReader<out List<CrawledJobPostingDto>> {
        val day = dayParam?: 0L
        return PostingReader(day)
    }

    @Bean
    fun postingProcessor(): ItemProcessor<in List<CrawledJobPostingDto>, out List<CrawledJobPosting>> {
        return PostingProcessor()
    }

    @Bean
    @StepScope
    fun postingWriter(
        @Value("#{jobParameters['day']}") dayParam: Long?
    ): ItemWriter<List<CrawledJobPosting>> {
        val day = dayParam?: 0L
        return PostingWriter(entityManagerFactory, redisTemplate, day)
    }
}