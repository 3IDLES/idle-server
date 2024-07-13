package com.swm.idle.application.common.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.swm.idle.application"])
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = ["com.swm.idle.application"])
class ApplicationConfig {
}
