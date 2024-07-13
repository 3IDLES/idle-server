package com.swm.idle.infrastructure.client.businessregistration.dto

data class BusinessRegistrationNumberValidationResponse(
    val items: List<Result>,
) {

    data class Result(
        val company: String,
        val bno: String,
    )

    // 기본 생성자 추가
    constructor() : this(emptyList())
}
