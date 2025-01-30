package com.swm.idle.batch.common.launcher

import com.swm.idle.batch.job.JobConfig
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CrawlingJobLauncher(
    private val jobLauncher: JobLauncher,
    private val jobRegistry: JobRegistry,
    private val crawlingJobConfig: JobConfig,
) {

    @Scheduled(cron = "0 0 23 * * *")
    fun scheduleJob() {
        val jobParameters: JobParameters = JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .toJobParameters()

        jobLauncher.run(crawlingJobConfig.crawlingJob(), jobParameters)
    }

    fun jobStart() {
        val jobParameters: JobParameters = JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .toJobParameters()

        jobLauncher.run(jobRegistry.getJob("crawlingJob"), jobParameters)
    }
}
