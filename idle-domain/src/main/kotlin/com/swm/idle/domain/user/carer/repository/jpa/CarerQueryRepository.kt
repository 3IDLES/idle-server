package com.swm.idle.domain.user.carer.repository.jpa

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.swm.idle.domain.user.carer.entity.jpa.Carer
import com.swm.idle.domain.user.carer.entity.jpa.QCarer.carer
import com.swm.idle.domain.user.carer.enums.CarerAccountStatus
import com.swm.idle.domain.user.carer.enums.JobSearchStatus
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository

@Repository
class CarerQueryRepository(
    private val jpaQueryFactory: JPAQueryFactory,
) {

    fun findAllByLocationWithinRadius(location: Point): List<Carer>? {
        return jpaQueryFactory
            .selectFrom(carer)
            .where(
                isExistInRange(location)
                    .and(carer.jobSearchStatus.eq(JobSearchStatus.YES))
                    .and(carer.carerAccountStatus.eq(CarerAccountStatus.ACTIVE))
            )
            .fetch()
    }

    private fun isExistInRange(location: Point): BooleanExpression {
        return Expressions.booleanTemplate(
            "ST_Contains(ST_BUFFER({0}, 5000), {1})",
            location,
            carer.location
        )
    }


}
