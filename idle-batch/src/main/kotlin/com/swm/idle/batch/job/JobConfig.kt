package com.swm.idle.batch.job

import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import com.swm.idle.batch.step.PostingProcessor
import com.swm.idle.batch.step.PostingReader
import com.swm.idle.batch.step.PostingReader.Companion.nextPage
import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
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
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import java.util.concurrent.atomic.AtomicInteger

@Configuration
@EnableBatchProcessing
class JobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory
) {

    @Bean
    fun crawlingJob(): Job {
        nextPage = AtomicInteger(1)
        return JobBuilder("crawlingJob", jobRepository)
            .start(crawlStep())
            .preventRestart()
            .build()
    }

    @Bean
    fun crawlStep(): Step {
        return StepBuilder("crawlStep", jobRepository)
            .chunk<List<CrawledJobPostingDto>,
                    List<CrawledJobPosting>>(1, transactionManager)
            .reader(postingReader())
            .processor(postingProcessor())
            .writer(postingWriter())
            .taskExecutor(taskExecutor())
            .allowStartIfComplete(true)
            .build()
    }

    @Bean
    fun taskExecutor(): SimpleAsyncTaskExecutor {
        return SimpleAsyncTaskExecutor().apply {
            this.setConcurrencyLimit(4)
        }
    }

    @Bean
    @StepScope
    fun postingReader(): ItemReader<out List<CrawledJobPostingDto>> = PostingReader()

    @Bean
    fun postingProcessor(): ItemProcessor<in List<CrawledJobPostingDto>, out List<CrawledJobPosting>> = PostingProcessor()

    @Bean
    fun postingWriter(): ItemWriter<List<CrawledJobPosting>> {
        val jpaWriter = JpaItemWriter<CrawledJobPosting>()
        jpaWriter.setEntityManagerFactory(entityManagerFactory)

        return ItemWriter { items ->
            val chunk = Chunk<CrawledJobPosting>()
            items.forEach { itemList ->
                chunk.addAll(itemList)
            }
            jpaWriter.write(chunk)
        }
    }
}