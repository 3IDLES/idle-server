package com.swm.idle.support.transfer.auth.center

import com.swm.idle.domain.user.center.entity.jpa.CenterManager
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(
    name = "CenterManagerForPendingResponse",
    description = "센터 관리자 인증 대기 목록 조회 응답"
)
data class CenterManagerForPendingResponse(
    val pendingCenterManagers: List<PendingCenterManagerDto>?,
) {

    data class PendingCenterManagerDto(
        @Schema(description = "센터 관리자 ID")
        val id: UUID,

        @Schema(description = "센터 관리자 로그인 ID")
        val identifier: String,

        @Schema(description = "센터 관리자 성명")
        val managerName: String,

        @Schema(description = "센터 사업자 등록 번호")
        val centerBusinessRegistrationNumber: String,

        @Schema(description = "센터 관리자 개인 연락처")
        val phoneNumber: String,
    ) {

        companion object {

            fun of(centerManager: CenterManager): PendingCenterManagerDto {
                return PendingCenterManagerDto(
                    id = centerManager.id,
                    identifier = centerManager.identifier,
                    managerName = centerManager.name,
                    centerBusinessRegistrationNumber = centerManager.centerBusinessRegistrationNumber,
                    phoneNumber = centerManager.phoneNumber
                )
            }

        }

    }

    companion object {

        fun of(pendingCenterManagerDtos: List<PendingCenterManagerDto>?): CenterManagerForPendingResponse {
            return CenterManagerForPendingResponse(pendingCenterManagerDtos)
        }

    }

}
