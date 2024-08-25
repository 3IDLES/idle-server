package com.swm.idle.support.transfer.jobposting.carer

import com.swm.idle.support.transfer.common.ScrollRequest
import io.swagger.v3.oas.annotations.Parameter
import org.springdoc.core.annotations.ParameterObject
import java.util.*

@ParameterObject
data class CursorScrollRequest(
    @Parameter(required = false, example = "다음 요청 시에 최초로 조회되는 ID. 최초 요청시에는 null")
    override val next: UUID? = null,
    @Parameter(required = false, example = "조회 item 수 : default 10")
    override val limit: Long = 10,
) : ScrollRequest<UUID?>(
    next = next,
    limit = limit,
)
