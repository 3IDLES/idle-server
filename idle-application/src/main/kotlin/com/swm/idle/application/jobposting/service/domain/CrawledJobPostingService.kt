package com.swm.idle.application.jobposting.service.domain

import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import com.swm.idle.domain.jobposting.repository.jpa.CrawledJobPostingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CrawledJobPostingService(
    private val crawledJobPostingRepository: CrawledJobPostingRepository,
) {

    @Transactional
    fun saveAll(crawledJobPostings: List<CrawledJobPosting>) {
        crawledJobPostingRepository.saveAll(crawledJobPostings)
    }

}
