plugins {
    id("jacoco")
    id("jacoco-report-aggregation")
    id("java-test-fixtures")
    id(libs.plugins.spring.boot.get().pluginId) version libs.plugins.spring.boot.get().version.toString()
    id(libs.plugins.spring.dependency.management.get().pluginId) version libs.plugins.spring.dependency.management.get().version.toString()
    id(libs.plugins.kotlin.jvm.get().pluginId) version libs.plugins.kotlin.jvm.get().version.toString()
    id(libs.plugins.kotlin.jpa.get().pluginId) version libs.plugins.kotlin.jpa.get().version.toString()
    id(libs.plugins.kotlin.spring.get().pluginId) version libs.plugins.kotlin.spring.get().version.toString()
}

allprojects {
    group = "com.swm"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java-test-fixtures")
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.jpa.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.spring.get().pluginId)
    apply(plugin = rootProject.libs.plugins.spring.boot.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.kapt.get().pluginId)
    apply(plugin = rootProject.libs.plugins.spring.dependency.management.get().pluginId)

    dependencies {
        implementation(rootProject.libs.kotlin.reflect)

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

    tasks.getByName("bootJar") {
        enabled = false
    }

    tasks.getByName("jar") {
        enabled = true
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
