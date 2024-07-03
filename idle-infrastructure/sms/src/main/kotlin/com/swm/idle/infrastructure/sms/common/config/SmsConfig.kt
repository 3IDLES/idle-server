package com.swm.idle.infrastructure.sms.common.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.swm.idle.infrastructure.sms"])
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = ["com.swm.idle.infrastructure.sms"])
class SmsConfig{
}
