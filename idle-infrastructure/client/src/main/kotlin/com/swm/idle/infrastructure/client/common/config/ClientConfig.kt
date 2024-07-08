package com.swm.idle.infrastructure.client.common.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.swm.idle.infrastructure.client"])
@EnableConfigurationProperties
@EnableFeignClients(basePackages = ["com.swm.idle.infrastructure.client"])
@ConfigurationPropertiesScan(basePackages = ["com.swm.idle.infrastructure.client"])
class ClientConfig {
}
