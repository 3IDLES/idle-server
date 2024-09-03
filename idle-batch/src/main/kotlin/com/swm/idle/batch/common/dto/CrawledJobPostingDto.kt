package com.swm.idle.batch.common.dto

import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import com.swm.idle.support.common.uuid.UuidCreator
import org.locationtech.jts.geom.Point
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class CrawledJobPostingDto(
    val title: String,
    val content: String,
    val clientAddress: String,
    val createdAt: String,
    val payInfo: String,
    val workTime: String,
    val workSchedule: String,
    val applyDeadline: String,
    val recruitmentProcess: String,
    val applyMethod: String,
    val requiredDocument: String,
    val centerName: String,
    val centerAddress: String,
    val directUrl: String,
) {

    fun parseApplyDeadline(applyDeadline: String): LocalDate {
        return if (applyDeadline.contains("채용시까지")) {
            LocalDate.now().plusMonths(1)
        } else {
            val dateString = applyDeadline.substringBefore("마감시간").trim()
            LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }
    }

    fun toDomain(location: Point): CrawledJobPosting {
        return CrawledJobPosting(
            id = UuidCreator.create(),
            title = title,
            content = content,
            clientAddress = clientAddress,
            createdAt = LocalDateTime.parse(
                createdAt,
                DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
            ),
            payInfo = payInfo,
            workTime = workTime,
            workSchedule = workSchedule,
            applyDeadline = parseApplyDeadline(applyDeadline),
            recruitmentProcess = recruitmentProcess,
            applyMethod = applyMethod,
            requiredDocument = requiredDocument,
            centerName = centerName,
            centerAddress = centerAddress,
            directUrl = directUrl,
            location = location,
        )
    }

}

