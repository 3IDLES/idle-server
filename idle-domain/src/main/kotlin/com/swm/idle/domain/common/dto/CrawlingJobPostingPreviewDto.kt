package com.swm.idle.domain.common.dto

import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting

data class CrawlingJobPostingPreviewDto(
    val crawledJobPosting: CrawledJobPosting,
    var distance: Int = 0,
    var isFavorite: Boolean = false,
) {

    constructor(
        crawledJobPosting: CrawledJobPosting,
    ) : this(crawledJobPosting = crawledJobPosting, distance = 0, isFavorite = false)

}
