package com.swm.idle.domain.common.entity

import com.swm.idle.domain.common.enums.EntityStatus
import com.swm.idle.support.common.uuid.UuidCreator
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
abstract class BaseEntity {

    @Id
    val id: UUID = UuidCreator.create()

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    var entityStatus: EntityStatus = EntityStatus.ACTIVE

    @CreationTimestamp
    @Column(columnDefinition = "timestamp", updatable = false)
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    @Column(columnDefinition = "timestamp")
    var updatedAt: LocalDateTime? = null
        protected set

    fun active() {
        this.entityStatus = EntityStatus.ACTIVE
    }

    fun delete() {
        this.entityStatus = EntityStatus.DELETED
    }

}
