package com.swm.idle.support.transfer.jobposting.common

import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(
    name = "CrawlingJobPostingResponse",
    description = "크롤링 공고 상세 조회 API",
)
class CrawlingJobPostingResponse(
    id: UUID,
    content: String?,
    clientAddress: String?,
    longitude: String?,
    latitude: String?,
    createdAt: String?,
    payInfo: String?,
    workingTime: String?,
    workingSchedule: String?,
    applyDeadline: String?,
    recruitmentProcess: String?,
    applyMethod: String?,
    requiredDocumentation: String?,
    centerName: String?,
    centerAddress: String?,
    jobPostingUrl: String?,
) {

    companion object {

        fun from(
            crawlingJobPosting: CrawledJobPosting,
            longitude: String,
            latitude: String,
        ): CrawlingJobPostingResponse {
            return CrawlingJobPostingResponse(
                id = crawlingJobPosting.id,
                content = crawlingJobPosting.content,
                clientAddress = crawlingJobPosting.clientAddress,
                longitude = longitude,
                latitude = latitude,
                createdAt = crawlingJobPosting.createdAt.toString(),
                payInfo = crawlingJobPosting.payInfo,
                workingTime = crawlingJobPosting.workTime,
                workingSchedule = crawlingJobPosting.workSchedule,
                applyDeadline = crawlingJobPosting.applyDeadline,
                recruitmentProcess = crawlingJobPosting.recruitmentProcess,
                applyMethod = crawlingJobPosting.applyMethod,
                requiredDocumentation = crawlingJobPosting.requiredDocument,
                centerName = crawlingJobPosting.centerName,
                centerAddress = crawlingJobPosting.centerAddress,
                jobPostingUrl = crawlingJobPosting.directUrl
            )
        }

    }
}
