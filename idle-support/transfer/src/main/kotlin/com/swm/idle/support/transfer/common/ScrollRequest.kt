package com.swm.idle.support.transfer.common

abstract class ScrollRequest<N>(
    open val next: N,
    open val limit: Long,
)
