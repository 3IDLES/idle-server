package com.swm.idle.presentation.batch.controller

import com.swm.idle.batch.common.launcher.CrawlingJobLauncher
import com.swm.idle.presentation.batch.api.BatchApi
import org.springframework.web.bind.annotation.RestController

@RestController
class BatchController(
    private val jobLauncher: CrawlingJobLauncher,
) : BatchApi {
    /**
 * Initiates a batch crawling job for the specified day.
 *
 * @param day The day for which the batch job should be started, represented as a long value.
 */
override fun launchBatch(day: Long) = jobLauncher.jobStart(day)
}