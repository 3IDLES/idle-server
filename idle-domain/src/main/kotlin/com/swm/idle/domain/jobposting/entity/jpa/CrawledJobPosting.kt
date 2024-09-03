package com.swm.idle.domain.jobposting.entity.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.locationtech.jts.geom.Point
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "crawled_job_posting")
class CrawledJobPosting(
    id: UUID,
    title: String,
    content: String,
    clientAddress: String,
    payInfo: String,
    workTime: String,
    workSchedule: String,
    applyDeadline: LocalDate,
    recruitmentProcess: String,
    applyMethod: String,
    requiredDocument: String,
    centerName: String,
    centerAddress: String,
    directUrl: String,
    createdAt: LocalDateTime,
    location: Point,
) {

    @Id
    @Column(nullable = false)
    var id: UUID = id
        private set

    @Column(columnDefinition = "varchar(255)")
    var title: String = title
        private set

    @Column(columnDefinition = "TEXT")
    var content: String = content
        private set

    @Column(columnDefinition = "varchar(255)")
    var clientAddress: String = clientAddress
        private set

    @Column(columnDefinition = "varchar(255)")
    var payInfo: String = payInfo
        private set

    @Column(columnDefinition = "varchar(255)")
    var workTime: String = workTime
        private set

    @Column(columnDefinition = "varchar(255)")
    var workSchedule: String = workSchedule
        private set

    var applyDeadline: LocalDate = applyDeadline
        private set

    @Column(columnDefinition = "varchar(255)")
    var recruitmentProcess: String = recruitmentProcess
        private set

    @Column(columnDefinition = "varchar(255)")
    var applyMethod: String = applyMethod
        private set

    @Column(columnDefinition = "varchar(255)")
    var requiredDocument: String = requiredDocument
        private set

    @Column(columnDefinition = "varchar(255)")
    var centerName: String = centerName
        private set

    @Column(columnDefinition = "varchar(255)")
    var centerAddress: String = centerAddress
        private set

    @Column(columnDefinition = "varchar(255)")
    var directUrl: String = directUrl
        private set

    @Column(columnDefinition = "varchar(255)")
    var createdAt: LocalDateTime = createdAt
        private set

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    var location: Point = location
        private set

}
