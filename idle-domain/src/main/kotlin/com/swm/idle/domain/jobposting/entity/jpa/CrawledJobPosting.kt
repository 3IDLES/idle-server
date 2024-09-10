package com.swm.idle.domain.jobposting.entity.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.locationtech.jts.geom.Point
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "crawled_job_posting")
class CrawledJobPosting(
    id: UUID,
    title: String?,
    content: String?,
    clientAddress: String,
    payInfo: String?,
    workTime: String?,
    workSchedule: String?,
    applyDeadline: String?,
    recruitmentProcess: String?,
    applyMethod: String?,
    requiredDocument: String?,
    centerName: String?,
    centerAddress: String,
    directUrl: String?,
    createdAt: LocalDateTime?,
    location: Point,
) {

    @Id
    @Column(nullable = true)
    var id: UUID = id
        private set

    @Column(columnDefinition = "varchar(255)", nullable = true)
    var title: String? = title
        private set

    @Column(columnDefinition = "TEXT", nullable = true)
    var content: String? = content
        private set

    @Column(columnDefinition = "varchar(255)", nullable = true)
    var clientAddress: String = clientAddress
        private set

    @Column(columnDefinition = "varchar(255)", nullable = true)
    var payInfo: String? = payInfo
        private set

    @Column(columnDefinition = "varchar(255)", nullable = true)
    var workTime: String? = workTime
        private set

    @Column(columnDefinition = "varchar(255)", nullable = true)
    var workSchedule: String? = workSchedule
        private set

    @Column(nullable = true)
    var applyDeadline: String? = applyDeadline
        private set

    @Column(columnDefinition = "varchar(255)", nullable = true)
    var recruitmentProcess: String? = recruitmentProcess
        private set

    @Column(columnDefinition = "varchar(255)", nullable = true)
    var applyMethod: String? = applyMethod
        private set

    @Column(columnDefinition = "varchar(255)", nullable = true)
    var requiredDocument: String? = requiredDocument
        private set

    @Column(columnDefinition = "varchar(255)", nullable = true)
    var centerName: String? = centerName
        private set

    @Column(columnDefinition = "varchar(255)")
    var centerAddress: String = centerAddress
        private set

    @Column(columnDefinition = "text", nullable = true)
    var directUrl: String? = directUrl
        private set

    @Column(columnDefinition = "timestamp", nullable = true)
    var createdAt: LocalDateTime? = createdAt
        private set

    @Column(columnDefinition = "POINT SRID 4326")
    var location: Point = location
        private set

}
