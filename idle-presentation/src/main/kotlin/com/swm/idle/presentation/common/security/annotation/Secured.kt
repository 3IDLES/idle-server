package com.swm.idle.presentation.common.security.annotation

import io.swagger.v3.oas.annotations.security.SecurityRequirement

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@SecurityRequirement(name = "AccessToken")
annotation class Secured
