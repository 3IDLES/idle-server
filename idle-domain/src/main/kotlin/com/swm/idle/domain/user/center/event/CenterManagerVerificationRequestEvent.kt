package com.swm.idle.domain.user.center.event

import com.swm.idle.domain.user.center.entity.jpa.CenterManager

data class CenterManagerVerificationRequestEvent(
    val centerManager: CenterManager,
) {

    companion object {

        fun CenterManager.createVerifyEvent(): CenterManagerVerificationRequestEvent {
            require(isNew()) { "인증 요청이 가능한 상태가 아닙니다." }

            return CenterManagerVerificationRequestEvent(this)
        }

    }

}
