package com.swm.idle.infrastructure.fcm.common.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.swm.idle.infrastructure.fcm"])
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = ["com.swm.idle.infrastructure.fcm"])
class FcmConfig {
}
