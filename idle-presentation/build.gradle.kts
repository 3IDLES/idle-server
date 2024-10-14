dependencies {
    implementation(project(":idle-domain"))
    implementation(project(":idle-application"))
    implementation(project(":idle-batch"))
    implementation(project(":idle-support:logging"))
    implementation(project(":idle-support:common"))
    implementation(project(":idle-support:transfer"))
    implementation(project(":idle-support:security"))
    implementation(project(":idle-infrastructure:aws"))
    implementation(project(":idle-infrastructure:client"))
    implementation(project(":idle-infrastructure:sms"))
    implementation(project(":idle-infrastructure:fcm"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    developmentOnly(libs.spring.boot.devtools)
//    developmentOnly(libs.spring.boot.docker.compose)
}
