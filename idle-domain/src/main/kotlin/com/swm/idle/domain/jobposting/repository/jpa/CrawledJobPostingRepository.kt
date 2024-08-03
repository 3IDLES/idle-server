package com.swm.idle.domain.jobposting.repository.jpa

import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CrawledJobPostingRepository : JpaRepository<CrawledJobPosting, UUID> {

}
