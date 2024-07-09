package com.swm.idle.domain.common.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.swm.idle.domain"])
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = ["com.swm.idle.domain"])
class DomainConfig {
}
