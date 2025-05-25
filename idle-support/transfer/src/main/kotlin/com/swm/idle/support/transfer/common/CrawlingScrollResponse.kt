package com.swm.idle.support.transfer.common

abstract class CrawlingScrollResponse<T, N>(
    open val items: List<T>,
    open val next: N,
    open val total: Int,
    open val nextDistance: Int
)
