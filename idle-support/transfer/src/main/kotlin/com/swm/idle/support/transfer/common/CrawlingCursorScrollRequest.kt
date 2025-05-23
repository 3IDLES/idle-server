package com.swm.idle.support.transfer.common

import io.swagger.v3.oas.annotations.Parameter
import org.springdoc.core.annotations.ParameterObject
import java.util.*

@ParameterObject
data class CrawlingCursorScrollRequest(
    @Parameter(required = false, example = "다음 요청 시에 최초로 조회되는 ID. 최초 요청시에는 null")
    override val next: UUID? = null,
    @Parameter(required = false, example = "조회 item 수 : default 10")
    override val limit: Long = 10,
    @Parameter(required = true, example = "조회 거리 : default 15km")
    override val distance: Long = 15,
) : CrawlingScrollRequest<UUID?>(
    next = next,
    limit = limit,
    distance = distance,
)