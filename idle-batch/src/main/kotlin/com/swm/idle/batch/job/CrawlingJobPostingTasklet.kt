package com.swm.idle.batch.job

import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.application.jobposting.domain.CrawlingJobPostingService
import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import com.swm.idle.batch.util.WorknetCrawler
import com.swm.idle.infrastructure.client.geocode.service.GeoCodeService
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

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val crawlingJobPostings: List<CrawledJobPostingDto>? = WorknetCrawler.run()

        if (crawlingJobPostings != null) {
            crawlingJobPostings.map { crawledJobPosting ->
                val clientLocationInfo = geoCodeService.search(crawledJobPosting.clientAddress)

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
                    location = clientLocation
                ).toDomain()
            }.let {
                crawlingJobPostingService.saveAll(it)
            }
        }

        return RepeatStatus.FINISHED
    }

}
