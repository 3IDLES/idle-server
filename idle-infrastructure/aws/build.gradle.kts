import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {
    implementation(project(":idle-domain"))
    implementation(libs.spring.boot.core)
    implementation(libs.spring.cloud.starter.aws.s3)
}
