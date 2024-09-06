package com.swm.idle.support.transfer.jobposting.carer

import com.swm.idle.domain.jobposting.enums.JobPostingType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "CreateJobPostingFavoriteRequest",
    description = "공고 즐겨찾기 등록 요청"
)
data class CreateJobPostingFavoriteRequest(
    @Schema(
        description = "공고 타입",
        allowableValues = ["WORKNET", "CAREMEET"]
    )
    val jobPostingType: JobPostingType,
)
