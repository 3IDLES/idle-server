package com.swm.idle.application.common.converter

import com.swm.idle.domain.user.carer.entity.jpa.Carer
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

    /**
     * Converts the latitude and longitude of a Carer entity into a JTS Point.
     *
     * The resulting Point uses the longitude as the x-coordinate and latitude as the y-coordinate.
     *
     * @param carer The Carer entity whose geographic coordinates are to be converted.
     * @return A Point representing the Carer's location.
     */
    fun convertToPoint(carer: Carer): Point {
        val latitude = carer.latitude.toDouble()
        val longitude = carer.longitude.toDouble()

        return geometryFactory.createPoint(Coordinate(longitude, latitude))
    }

    /**
     * Converts latitude and longitude coordinates to a JTS `Point` object.
     *
     * The longitude is used as the x-coordinate and the latitude as the y-coordinate.
     *
     * @param latitude The latitude value.
     * @param longitude The longitude value.
     * @return A `Point` representing the specified geographic location.
     */
    fun convertToPoint(latitude: Double, longitude: Double ): Point {
        return geometryFactory.createPoint(Coordinate(longitude, latitude))
    }
}