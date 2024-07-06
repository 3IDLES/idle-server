package com.swm.idle.domain.common.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.swm.idle.domain"])
@EntityScan(basePackages = ["com.swm.idle.domain"])
class PersistenceConfig {
}
