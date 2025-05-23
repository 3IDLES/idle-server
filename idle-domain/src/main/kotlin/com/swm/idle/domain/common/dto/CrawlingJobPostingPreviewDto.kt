package com.swm.idle.domain.common.dto

import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting

data class CrawlingJobPostingPreviewDto(
    val crawledJobPosting: CrawledJobPosting,
    var distance: Int = 0,
    var isFavorite: Boolean = false,
) {

    constructor(
        crawledJobPosting: CrawledJobPosting,
        distance: Int
    ) : this(crawledJobPosting = crawledJobPosting, distance = distance, isFavorite = false)

    constructor(
        crawledJobPosting: CrawledJobPosting,
        isFavorite: Boolean,
    ) : this(crawledJobPosting = crawledJobPosting, distance = 0, isFavorite = isFavorite)

}
