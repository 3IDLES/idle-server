dependencies {
    implementation(project(":idle-domain"))
    implementation(project(":idle-support:logging"))
    implementation(project(":idle-support:common"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)

    developmentOnly(libs.spring.boot.devtools)
    developmentOnly(libs.spring.boot.docker.compose)
}
