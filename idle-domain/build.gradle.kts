dependencies {
    implementation(project(":idle-support:common"))

    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)

    runtimeOnly(libs.mysql.connector.java)
}
