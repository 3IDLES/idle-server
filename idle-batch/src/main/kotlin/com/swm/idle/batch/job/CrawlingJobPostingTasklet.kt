package com.swm.idle.batch.job

import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.application.jobposting.domain.CrawlingJobPostingService
import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import com.swm.idle.batch.util.WorknetCrawler
import com.swm.idle.infrastructure.client.geocode.service.GeoCodeService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

@Component
class CrawlingJobPostingTasklet(
    private val crawlingJobPostingService: CrawlingJobPostingService,
    private val geoCodeService: GeoCodeService,
) : Tasklet {

    private val logger = KotlinLogging.logger { }

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val crawlingJobPostings: List<CrawledJobPostingDto>? = try {
            WorknetCrawler.run()
        } catch (e: Exception) {
            logger.warn {
                e.toString()
            }
            e.printStackTrace()  // 오류 로그 출력
            null  // 오류 발생
        }

        if (crawlingJobPostings != null) {
            crawlingJobPostings.mapNotNull { crawledJobPosting ->
                val clientRoadNameAddress = extractRoadNameAddress(crawledJobPosting.clientAddress)
                val clientLocationInfo = geoCodeService.search(clientRoadNameAddress)

                if (clientLocationInfo.addresses.isEmpty()) {
                    return@mapNotNull null
                }

                val clientLocation = PointConverter.convertToPoint(
                    latitude = clientLocationInfo.addresses[0].y.toDouble(),
                    longitude = clientLocationInfo.addresses[0].x.toDouble(),
                )

                CrawledJobPostingDto(
                    title = crawledJobPosting.title,
                    content = crawledJobPosting.content,
                    clientAddress = crawledJobPosting.clientAddress,
                    createdAt = crawledJobPosting.createdAt,
                    payInfo = crawledJobPosting.payInfo,
                    workTime = crawledJobPosting.workTime,
                    workSchedule = crawledJobPosting.workSchedule,
                    applyDeadline = crawledJobPosting.applyDeadline,
                    recruitmentProcess = crawledJobPosting.recruitmentProcess,
                    applyMethod = crawledJobPosting.applyMethod,
                    requiredDocument = crawledJobPosting.requiredDocument,
                    centerName = crawledJobPosting.centerName,
                    centerAddress = crawledJobPosting.centerAddress,
                    directUrl = crawledJobPosting.directUrl,
                ).toDomain(clientLocation)
            }.let {
                println("크롤링된 data 크기 : ${it.size}")
                crawlingJobPostingService.saveAll(it)
            }

            return RepeatStatus.FINISHED
        }

        return RepeatStatus.FINISHED
    }

    private fun extractRoadNameAddress(clientAddress: String): String {
        return clientAddress.replace(Regex(REGEX_FORMAT), "")
            .substringBefore(SPLIT_DELIMITER)
            .trim()
    }

    companion object {

        const val REGEX_FORMAT = "\\(.*?\\)"
        const val SPLIT_DELIMITER = ","
    }

}
