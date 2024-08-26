package com.swm.idle.application.jobposting.domain

import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import com.swm.idle.domain.jobposting.repository.jpa.CrawledJobPostingJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CrawledJobPostingService(
    private val crawledJobPostingJpaRepository: CrawledJobPostingJpaRepository,
) {

    @Transactional
    fun saveAll(crawledJobPostings: List<CrawledJobPosting>) {
        crawledJobPostingJpaRepository.saveAll(crawledJobPostings)
    }

}
