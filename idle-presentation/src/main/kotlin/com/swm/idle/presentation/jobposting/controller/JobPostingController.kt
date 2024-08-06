package com.swm.idle.presentation.jobposting.controller

import com.swm.idle.application.jobposting.service.facade.JobPostingFacadeService
import com.swm.idle.presentation.jobposting.api.JobPostingApi
import com.swm.idle.support.transfer.jobposting.CreateJobPostingRequest
import org.springframework.web.bind.annotation.RestController

@RestController
class JobPostingController(
    private val jobPostingFacadeService: JobPostingFacadeService,
) : JobPostingApi {

    override fun createJobPosting(request: CreateJobPostingRequest) {
        jobPostingFacadeService.create(request = request)
    }

}
