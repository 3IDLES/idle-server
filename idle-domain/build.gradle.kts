import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {
    implementation(project(":idle-support:common"))
    implementation(project(":idle-support:security"))

    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.querydsl.spatial)
    implementation(libs.hibernate.spatial)
    implementation(libs.flyway.core)
    implementation(libs.flyway.mysql)
    runtimeOnly(libs.mysql.connector.java)

    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")

    testImplementation("org.testcontainers:junit-jupiter:1.18.3")
    testImplementation("com.h2database:h2")
    testImplementation("it.ozimov:embedded-redis:0.7.2")
}
