package com.swm.idle.domain.common.dto

import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.entity.jpa.JobPostingWeekday
import java.time.LocalDateTime

data class JobPostingPreviewDto(
    val jobPosting: JobPosting,
    val jobPostingWeekdays: List<JobPostingWeekday>,
    var distance: Int = 0,
    val applyTime: LocalDateTime?,
    val isFavorite: Boolean,
) {

    constructor(
        jobPosting: JobPosting,
        jobPostingWeekdays: List<JobPostingWeekday>,
        applyTime: LocalDateTime?,
    ) : this(jobPosting, jobPostingWeekdays, 0, applyTime, true)

    constructor(
        jobPosting: JobPosting,
        jobPostingWeekdays: List<JobPostingWeekday>,
        applyTime: LocalDateTime?,
        isFavorite: Boolean,
    ) : this(jobPosting, jobPostingWeekdays, 0, applyTime, isFavorite)

}
