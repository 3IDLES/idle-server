import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {
    implementation(project(":idle-infrastructure:aws"))
    implementation(project(":idle-infrastructure:client"))
    implementation(project(":idle-infrastructure:sms"))
    implementation(project(":idle-support:common"))
    implementation(project(":idle-support:security"))
    implementation(project(":idle-support:transfer"))
    implementation(project(":idle-domain"))

    implementation(libs.spring.tx)
    implementation(libs.spring.boot.core)

    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)
}
