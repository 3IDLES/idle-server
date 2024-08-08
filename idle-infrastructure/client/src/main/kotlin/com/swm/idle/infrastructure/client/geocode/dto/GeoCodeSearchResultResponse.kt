package com.swm.idle.infrastructure.client.geocode.dto

data class GeoCodeSearchResultResponse(
    val addresses: List<AddressInfo>,
) {

    data class AddressInfo(
        val roadAddress: String,
        val jibunAddress: String,
        val x: String,
        val y: String,
        val distance: Double,
    )

    constructor() : this(emptyList())

}
