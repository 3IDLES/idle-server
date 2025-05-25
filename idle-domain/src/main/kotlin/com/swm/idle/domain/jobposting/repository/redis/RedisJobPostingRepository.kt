package com.swm.idle.domain.jobposting.repository.redis

import org.locationtech.jts.geom.Point
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.GeoResults
import org.springframework.data.geo.Metrics
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Repository
class RedisJobPostingRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    /**
     * Retrieves a list of job posting UUIDs located within a specified distance from a given geographic point, with optional pagination.
     *
     * If `next` is null, returns up to `limit` UUIDs of job postings within the radius. If `next` is provided, returns the next page of UUIDs after the specified UUID, ordered by proximity.
     *
     * @param location The geographic point to search around.
     * @param distance The search radius in kilometers.
     * @param limit The maximum number of UUIDs to return.
     * @param next The UUID to start pagination after, or null to start from the beginning.
     * @return A list of job posting UUIDs matching the location and distance criteria, paginated if `next` is provided.
     */
    fun findByLocationAndDistance(
        location: Point,
        distance: Long,
        limit: Long,
        next: UUID?
    ): List<UUID> {
        val keys = makeRedisKeys()
        val circle = Circle(
            org.springframework.data.geo.Point(location.x, location.y),
            Distance(distance.toDouble(),
                Metrics.KILOMETERS)
        )

        if(next == null) {//first
            val ids = findPostingIds(keys, circle)
            return ids.take(limit.toInt())
        }

        return getPagedGeoUuids(keys, circle, next, limit)
    }

    /**
     * Generates Redis keys for job postings geospatial data for the current day and the previous 12 days.
     *
     * @return A list of Redis key strings in the format "job_postings_geo_YYYYMMDD" for the last 13 days.
     */
    private fun makeRedisKeys(): List<String> {
        val today = LocalDate.now()
        val keys = (0..12).map { offset ->
            val date = today.minusDays(offset.toLong())
            "job_postings_geo_${date.format(DateTimeFormatter.BASIC_ISO_DATE)}"
        }
        return keys
    }

    /**
     * Retrieves all job posting UUIDs within the specified geospatial circle across multiple Redis keys.
     *
     * Iterates through the provided Redis keys, performing a geospatial radius query for each,
     * and collects UUIDs of job postings located within the given circle.
     *
     * @param keys List of Redis keys to search for job postings.
     * @param circle Geospatial area to search within.
     * @return Mutable list of UUIDs for job postings found within the specified area.
     */
    private fun findPostingIds(
        keys: List<String>,
        circle: Circle
    ): MutableList<UUID> {
        val ids = mutableListOf<UUID>()

        keys.forEach { key ->
            val geoResults = redisTemplate.opsForGeo().radius(key, circle) as GeoResults<GeoLocation<String>>

            val uuids = geoResults.content.mapNotNull { geoLocation ->
                UUID.fromString(geoLocation.content.name)
            }
            ids.addAll(uuids)
        }
        return ids
    }

    /**
     * Retrieves a paginated list of job posting UUIDs within a specified geospatial area, starting after a given UUID.
     *
     * Collects UUIDs from the provided Redis keys that fall within the given circle, sorts them by ascending distance from the center, and returns up to the specified limit of UUIDs following the `next` UUID.
     *
     * @param keys List of Redis keys to search for geospatial job postings.
     * @param circle The geospatial area to search within.
     * @param next The UUID after which to start pagination.
     * @param limit The maximum number of UUIDs to return.
     * @return A list of UUIDs representing job postings after the `next` UUID, ordered by proximity.
     */
    fun getPagedGeoUuids(
        keys: List<String>,
        circle: Circle,
        next: UUID,
        limit: Long
    ): List<UUID> {
        val distanceMap = mutableListOf<Pair<UUID, Double>>()

        keys.forEach { key ->
            val geoResults = redisTemplate.opsForGeo().radius(key, circle) as GeoResults<GeoLocation<String>>
            geoResults.content.forEach { geoLocation ->
                val uuid = runCatching { UUID.fromString(geoLocation.content.name) }.getOrNull()
                val distance = geoLocation.distance?.value
                if (uuid != null && distance != null) {
                    distanceMap.add(uuid to distance)
                }
            }
        }

        val sortedIdsByDistance = distanceMap.sortedBy { it.second }.map { it.first }

        val result = mutableListOf<UUID>()
        var foundNext = false

        for (id in sortedIdsByDistance) {
            if (foundNext) {
                result.add(id)
                if (result.size == limit.toInt()) break
            }

            if (id == next) foundNext = true
        }

        return result
    }
}