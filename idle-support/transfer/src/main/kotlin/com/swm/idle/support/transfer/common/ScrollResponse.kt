package com.swm.idle.support.transfer.common

abstract class ScrollResponse<T, N>(
    open val items: List<T>,
    open val next: N,
    open val total: Int,
)
