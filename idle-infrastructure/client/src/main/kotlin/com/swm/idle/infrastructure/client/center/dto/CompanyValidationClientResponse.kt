package com.swm.idle.infrastructure.client.center.dto

data class CompanyValidationClientResponse(
    val items: List<Result>,
) {

    data class Result(
        val company: String,
        val bno: String,
    )

    // 기본 생성자 추가
    constructor() : this(emptyList())
}
