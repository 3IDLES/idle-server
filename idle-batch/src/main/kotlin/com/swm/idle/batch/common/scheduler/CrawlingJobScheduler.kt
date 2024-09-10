package com.swm.idle.batch.common.scheduler

import com.swm.idle.batch.job.CrawlingJobConfig
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CrawlingJobScheduler(
    private val jobLauncher: JobLauncher,
    private val crawlingJobConfig: CrawlingJobConfig,
) {

    @Scheduled(cron = "0 0 2 * * *")
    fun scheduleJob() {
        val jobParameters: JobParameters = JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .toJobParameters()
        jobLauncher.run(crawlingJobConfig.crawlingJob(), jobParameters)
    }

}

