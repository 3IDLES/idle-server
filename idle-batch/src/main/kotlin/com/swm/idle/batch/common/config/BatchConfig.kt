package com.swm.idle.batch.common.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.swm.idle.batch"])
@EnableAutoConfiguration
class BatchConfig {
}
