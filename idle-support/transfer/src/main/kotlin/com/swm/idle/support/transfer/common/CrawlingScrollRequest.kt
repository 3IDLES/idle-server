package com.swm.idle.support.transfer.common

abstract class CrawlingScrollRequest<N>(
    open val next: N,
    open val limit: Long,
    open val distance: Long,
)