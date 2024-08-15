package com.swm.idle.application.common.converter

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel

object PointConverter {

    private const val SPATIAL_REFERENCE_IDENTIFIER_NUMBER: Int = 4326

    private val geometryFactory: GeometryFactory = GeometryFactory(
        PrecisionModel(),
        SPATIAL_REFERENCE_IDENTIFIER_NUMBER
    )

    fun convertToPoint(
        latitude: Double,
        longitude: Double,
    ): Point {
        return geometryFactory.createPoint(Coordinate(longitude, latitude))
    }

}
