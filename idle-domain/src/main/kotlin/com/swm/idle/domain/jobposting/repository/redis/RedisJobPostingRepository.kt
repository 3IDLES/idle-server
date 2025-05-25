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
    companion object {
            private const val REDIS_TTL_DAYS = 13L
            private const val REDIS_KEY_PREFIX = "job_postings_geo_"
        }

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

    private fun makeRedisKeys(): List<String> {
        val today = LocalDate.now()
        val keys = (0 until REDIS_TTL_DAYS).map { offset ->
            val date = today.minusDays(offset)
            "$REDIS_KEY_PREFIX${date.format(DateTimeFormatter.BASIC_ISO_DATE)}"
        }
        return keys
    }

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