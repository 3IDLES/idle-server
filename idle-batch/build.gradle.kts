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
    implementation(rootProject.libs.selenium.java)
    implementation(rootProject.libs.selenium.chrome.driver)
    implementation(rootProject.libs.selenium.remote.driver)
    implementation(libs.mysql.connector.java)
}
