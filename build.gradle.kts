plugins {
    id("jacoco")
    id("jacoco-report-aggregation")
    id("java-test-fixtures")
    id(libs.plugins.sonarqube.get().pluginId) version libs.plugins.sonarqube.get().version.toString()
    id(libs.plugins.spring.boot.get().pluginId) version libs.plugins.spring.boot.get().version.toString()
    id(libs.plugins.spring.dependency.management.get().pluginId) version libs.plugins.spring.dependency.management.get().version.toString()
    id(libs.plugins.kotlin.jvm.get().pluginId) version libs.plugins.kotlin.jvm.get().version.toString()
    id(libs.plugins.kotlin.jpa.get().pluginId) version libs.plugins.kotlin.jpa.get().version.toString()
    id(libs.plugins.kotlin.spring.get().pluginId) version libs.plugins.kotlin.spring.get().version.toString()
    id(libs.plugins.kotlin.kapt.get().pluginId) version libs.plugins.kotlin.kapt.get().version.toString()
}

allprojects {
    group = "com.swm"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "jacoco")
    apply(plugin = "java-test-fixtures")
    apply(plugin = "org.sonarqube")
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.jpa.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.spring.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.kapt.get().pluginId)
    apply(plugin = rootProject.libs.plugins.spring.boot.get().pluginId)
    apply(plugin = rootProject.libs.plugins.spring.dependency.management.get().pluginId)

    dependencies {
        implementation(rootProject.libs.kotlin.logging)
        implementation(rootProject.libs.kotlin.reflect)
        implementation(rootProject.libs.kotlin.coroutines.core)
        implementation(rootProject.libs.kotlin.coroutines.reactor)
        implementation(rootProject.libs.uuid.creator)
        implementation(rootProject.libs.jbcrypt)
        implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
        implementation("com.querydsl:querydsl-apt:5.1.0:jakarta")
        implementation(rootProject.libs.locationtech)

        kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
        kapt(rootProject.libs.spring.boot.configuration.processor)

        testImplementation(rootProject.libs.mockk)
        testImplementation(rootProject.libs.spring.mockk)
        testImplementation(rootProject.libs.kotest.runner.junit5)
        testImplementation(rootProject.libs.kotest.assertions.core)
        testImplementation(rootProject.libs.kotest.extensions.spring)
        testImplementation(rootProject.libs.spring.boot.starter.test)
        testImplementation(rootProject.libs.kotest.extensions.testcontainers)
        testImplementation(rootProject.libs.testcontainers.junit.jupiter)
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(rootProject.libs.versions.java.get())
        }
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

}

sonar {
    properties {
        property("sonar.projectKey", "3IDLES_idle-server")
        property("sonar.organization", "3idles")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.java.coveragePlugin", "jacoco")
    }
}

val allProjects = project.allprojects
    .asSequence()
    .filter { it.name != "idle-server" }
    .filter { it.name != "idle-support" }
    .filter { it.name != "idle-domain" }
    .filter { it.name != "idle-infrastructure" }
    .filter { it.name != "idle-application" }
    .filter { it.name != "idle-presentation" }
    .filter { it.name != "idle-batch" }
    .toList()

project(":idle-support:jacoco") {
    apply(plugin = "jacoco-report-aggregation")

    dependencies {
        allProjects.forEach {
            add("jacocoAggregation", project(it.path))
        }
    }
}

springBoot {
    mainClass.set("com.swm.IdleServerApplicationKt")
}
