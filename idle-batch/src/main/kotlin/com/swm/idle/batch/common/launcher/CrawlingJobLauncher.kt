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
) {

    /**
     * Schedules and launches the "crawlingJob" batch job daily at 22:00 with the current timestamp and a day offset of 0.
     */
    @Scheduled(cron = "0 0 22 * * *")
    fun scheduleJob() {
        val jobParameters: JobParameters = JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .addLong("day", 0L)
            .toJobParameters()

        jobLauncher.run(jobRegistry.getJob("crawlingJob"), jobParameters)
    }


    /**
     * Manually triggers the "crawlingJob" batch job with a specified day offset.
     *
     * @param dayOffset The offset in days to include as a job parameter. Defaults to 0.
     */
    fun jobStart(dayOffset: Long = 0) {
        val jobParameters: JobParameters = JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .addLong("day", dayOffset)
            .toJobParameters()

        jobLauncher.run(jobRegistry.getJob("crawlingJob"), jobParameters)
    }
}
