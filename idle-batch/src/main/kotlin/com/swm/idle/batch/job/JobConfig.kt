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

    /**
     * Defines the "crawlingJob" batch job, starting with the provided step and preventing restarts after completion.
     *
     * @param crawlStep The initial step to execute in the job.
     * @return The configured batch job instance.
     */
    @Bean
    fun crawlingJob(crawlStep: Step): Job {
        return JobBuilder("crawlingJob", jobRepository)
            .start(crawlStep)
            .preventRestart()
            .build()
    }

    /**
     * Defines the batch step for crawling job postings, configuring chunk-oriented processing with parallel execution.
     *
     * The step reads lists of `CrawledJobPostingDto`, processes them into lists of `CrawledJobPosting`, and writes the results.
     * Chunk size is set to 1, and the step allows parallel execution with a concurrency limit. The step can be restarted even if previously completed.
     *
     * @return The configured batch step for crawling job postings.
     */
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

    /**
     * Creates a SimpleAsyncTaskExecutor with a concurrency limit of 4 for parallel task execution.
     *
     * @return A SimpleAsyncTaskExecutor configured to allow up to 4 concurrent tasks.
     */
    @Bean
    fun taskExecutor(): SimpleAsyncTaskExecutor {
        return SimpleAsyncTaskExecutor().apply {
            this.concurrencyLimit = 4
        }
    }

    /**
     * Creates a step-scoped `ItemReader` that reads lists of `CrawledJobPostingDto` based on the provided "day" job parameter.
     *
     * @param dayParam The "day" job parameter used to determine which postings to read; defaults to 0 if not provided.
     * @return An `ItemReader` that supplies lists of crawled job posting DTOs for the batch step.
     */
    @Bean
    @StepScope
    fun postingReader(
        @Value("#{jobParameters['day']}") dayParam: Long?
    ): ItemReader<out List<CrawledJobPostingDto>> {
        val day = dayParam?: 0L
        return PostingReader(day)
    }

    /**
     * Provides an item processor that converts lists of `CrawledJobPostingDto` objects into lists of `CrawledJobPosting` entities.
     *
     * @return An `ItemProcessor` for transforming DTOs to entities in batch processing.
     */
    @Bean
    fun postingProcessor(): ItemProcessor<in List<CrawledJobPostingDto>, out List<CrawledJobPosting>> {
        return PostingProcessor()
    }

    /**
     * Creates a step-scoped writer for persisting lists of crawled job postings.
     *
     * @param dayParam The job parameter indicating the day offset for processing; defaults to 0 if not provided.
     * @return An ItemWriter that writes lists of CrawledJobPosting entities using the configured entity manager and Redis template.
     */
    @Bean
    @StepScope
    fun postingWriter(
        @Value("#{jobParameters['day']}") dayParam: Long?
    ): ItemWriter<List<CrawledJobPosting>> {
        val day = dayParam?: 0L
        return PostingWriter(entityManagerFactory, redisTemplate, day)
    }
}