package com.swm.idle.support.transfer.user.center

import com.swm.idle.domain.user.center.entity.jpa.CenterManager
import com.swm.idle.domain.user.center.enums.CenterManagerAccountStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(
    name = "JoinStatusInfoResponse",
    description = "센터 관리자 회원가입 상태 응답"
)
data class JoinStatusInfoResponse(
    val id: UUID,
    val managerName: String,
    val phoneNumber: String,
    val centerManagerAccountStatus: CenterManagerAccountStatus,
) {

    companion object {

        fun from(centerManager: CenterManager): JoinStatusInfoResponse {
            return JoinStatusInfoResponse(
                id = centerManager.id,
                managerName = centerManager.name,
                phoneNumber = centerManager.phoneNumber,
                centerManagerAccountStatus = centerManager.status
            )
        }
    }
}
