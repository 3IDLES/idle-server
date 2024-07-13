package com.swm.idle.infrastructure.aws.common

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.swm.idle.infrastructure.aws"])
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = ["com.swm.idle.infrastructure.aws"])
class AwsConfig {
}
