import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {
    implementation(project(":idle-support:common"))
    implementation(project(":idle-domain"))
    implementation(project(":idle-application"))
    implementation(project(":idle-infrastructure:client"))

    implementation(rootProject.libs.spring.boot.starter.batch)
    implementation("org.seleniumhq.selenium:selenium-java:4.24.0")
//    implementation("org.seleniumhq.selenium:selenium-remote-driver:4.24.0")
    implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.24.0")
    implementation(libs.mysql.connector.java)
}
