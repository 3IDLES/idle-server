package com.swm.idle.infrastructure.fcm.common.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.swm.idle.infrastructure.fcm"])
interface FcmConfig {
}
