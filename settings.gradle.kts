rootProject.name = "idle-server"

include(":idle-presentation")
project(":idle-presentation").projectDir = file("idle-presentation")

include(":idle-application")
project(":idle-application").projectDir = file("idle-application")

include(":idle-domain")
project(":idle-domain").projectDir = file("idle-domain")

include(":idle-batch")
project(":idle-batch").projectDir = file("idle-batch")

// infrastructure modules
include(":idle-infrastructure:aws")
project(":idle-infrastructure:aws").projectDir = file("idle-infrastructure/aws")

include(":idle-infrastructure:client")
project(":idle-infrastructure:client").projectDir = file("idle-infrastructure/client")

include(":idle-infrastructure:sms")
project(":idle-infrastructure:sms").projectDir = file("idle-infrastructure/sms")

include(":idle-infrastructure:fcm")
project(":idle-infrastructure:fcm").projectDir = file("idle-infrastructure/fcm")

// support modules
include(":idle-support:common")
project(":idle-support:common").projectDir = file("idle-support/common")

include(":idle-support:jacoco")
project(":idle-support:jacoco").projectDir = file("idle-support/jacoco")

include(":idle-support:logging")
project(":idle-support:logging").projectDir = file("idle-support/logging")

include(":idle-support:transfer")
project(":idle-support:transfer").projectDir = file("idle-support/transfer")

include(":idle-support:security")
project(":idle-support:security").projectDir = file("idle-support/security")
