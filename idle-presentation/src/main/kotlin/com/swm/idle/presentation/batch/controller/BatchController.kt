package com.swm.idle.presentation.batch.controller

import com.swm.idle.batch.common.launcher.CrawlingJobLauncher
import com.swm.idle.presentation.batch.api.BatchApi
import org.springframework.web.bind.annotation.RestController

@RestController
class BatchController(
    private val jobLauncher: CrawlingJobLauncher,
) : BatchApi {
    override fun launchBatch() = jobLauncher.jobStart()
}