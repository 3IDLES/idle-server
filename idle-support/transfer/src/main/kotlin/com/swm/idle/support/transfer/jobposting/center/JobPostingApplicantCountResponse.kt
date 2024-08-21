package com.swm.idle.support.transfer.jobposting.center

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "JobPostingApplicantCountResponse",
    description = "공고 지원자 수 조회 응답",
)
data class JobPostingApplicantCountResponse(
    val applicantCount: Int = 0,
)
