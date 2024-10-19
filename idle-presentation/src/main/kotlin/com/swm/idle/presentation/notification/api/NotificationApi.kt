package com.swm.idle.presentation.notification.api

import com.swm.idle.presentation.common.exception.ErrorResponse
import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.jobposting.carer.CursorScrollRequest
import com.swm.idle.support.transfer.notification.NotificationScrollResponse
import com.swm.idle.support.transfer.notification.UnreadNotificationCountResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@Tag(name = "Notification", description = "알림 API")
@RequestMapping("/api/v1/notifications", produces = ["application/json;charset=utf-8"])
interface NotificationApi {

    @Secured
    @Operation(summary = "알림 읽음 처리 API")
    @PatchMapping("/{notification-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "처리 성공",
            ),
            ApiResponse(
                responseCode = "400",
                description = "처리 실패 - 본인이 아닌, 인증되지 않은 사용자가 읽음 처리를 시도한 경우",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = ErrorResponse::class
                        )
                    ),
                ]
            ),
        ]
    )
    fun readNotification(@PathVariable("notification-id") notificationId: UUID)

    @Secured
    @Operation(summary = "읽지 않은 알림 수 집계 API")
    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    fun countUnreadNotification(): UnreadNotificationCountResponse

    @Secured
    @Operation(summary = "알림 전체 조회 API")
    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    fun getNotifications(request: CursorScrollRequest): NotificationScrollResponse

}
