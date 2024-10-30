import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {
    implementation(project(":idle-support:common"))
    implementation(project(":idle-support:security"))

    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.querydsl.spatial)
    implementation(libs.hibernate.spatial)
    implementation(libs.flyway.core)
    implementation(libs.flyway.mysql)

    runtimeOnly(libs.mysql.connector.java)

}
