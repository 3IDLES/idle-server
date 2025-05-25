package com.swm.idle.batch.step

import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.data.geo.Point
import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PostingWriter(
    private val entityManagerFactory: EntityManagerFactory,
    private val redisTemplate: RedisTemplate<String, String>,
    private val dayOffset: Long
) : ItemWriter<List<CrawledJobPosting>> {

    private val jpaWriter = JpaItemWriter<CrawledJobPosting>().apply {
        setEntityManagerFactory(entityManagerFactory)
    }

    override fun write(chunk: Chunk<out List<CrawledJobPosting>>) {
        val flatChunk = Chunk<CrawledJobPosting>()
        chunk.forEach { itemList ->
            flatChunk.addAll(itemList)
        }

        jpaWriter.write(flatChunk)

        chunk.forEach { itemList ->
            saveToRedis(itemList)
        }
    }

    private fun saveToRedis(itemList: List<CrawledJobPosting>) {
        val today = LocalDate.now()
        val redisKey = "job_postings_geo_${today.format(DateTimeFormatter.BASIC_ISO_DATE)}"  // 예: job_postings_geo_20240520

        itemList.forEach { posting ->
            val added = redisTemplate.opsForGeo().add(
                redisKey,
                Point(posting.location.x, posting.location.y),
                posting.id.toString()
            )
        }

        redisTemplate.expire(redisKey, Duration.ofDays(13-dayOffset))
    }
}