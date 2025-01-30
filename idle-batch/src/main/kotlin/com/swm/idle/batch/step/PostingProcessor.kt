package com.swm.idle.batch.step

import com.swm.idle.batch.common.dto.CrawledJobPostingDto
import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import com.swm.idle.infrastructure.client.geocode.service.GeoCodeService
import org.springframework.batch.item.ItemProcessor

class PostingProcessor : ItemProcessor<List<CrawledJobPostingDto>, List<CrawledJobPosting>> {

    companion object {
        const val REGEX_FORMAT = "\\(.*?\\)"
        const val SPLIT_DELIMITER = ","
    }

    override fun process(item: List<CrawledJobPostingDto>): List<CrawledJobPosting> {
        return item.asSequence().mapNotNull {
            val roadNameAddress = extractRoadNameAddress(it.clientAddress)
            val geoInfo = GeoCodeService.search(roadNameAddress)
            if (geoInfo.addresses.isEmpty()) {
                return@mapNotNull null
            }
            val clientLocation = PointConverter.convertToPoint(
                latitude = geoInfo.addresses[0].y.toDouble(),
                longitude = geoInfo.addresses[0].x.toDouble(),
            )
            it.toDomain(clientLocation)
        }.toList()
    }

    private fun extractRoadNameAddress(clientAddress: String): String {
        return clientAddress.replace(Regex(REGEX_FORMAT), "")
            .substringBefore(SPLIT_DELIMITER)
            .trim()
    }
}