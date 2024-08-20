package com.swm.idle.support.transfer.applys

import com.swm.idle.domain.jobposting.vo.ApplyMethodType
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(
    name = "CreateApplyRequest",
    description = "공고 지원 요청"
)
data class CreateApplyRequest(
    val jobPostingId: UUID,
    val applyMethodType: ApplyMethodType,
)
