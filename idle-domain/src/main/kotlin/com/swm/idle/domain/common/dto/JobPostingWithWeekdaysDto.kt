package com.swm.idle.domain.common.dto

import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.entity.jpa.JobPostingWeekday

data class JobPostingWithWeekdaysDto(
    val jobPosting: JobPosting,
    val jobPostingWeekdays: List<JobPostingWeekday>,
    var distance: Int = 0,
) {

    constructor(jobPosting: JobPosting, jobPostingWeekdays: List<JobPostingWeekday>) :
            this(jobPosting, jobPostingWeekdays, 0)
}

