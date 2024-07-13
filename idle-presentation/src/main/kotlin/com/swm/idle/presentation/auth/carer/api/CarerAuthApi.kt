package com.swm.idle.presentation.auth.carer.api

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Auth-Carer", description = "Carer Auth API")
@RequestMapping("/api/v1/auth/carer", produces = ["application/json"])
interface CarerAuthApi {

}
