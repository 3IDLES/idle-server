rootProject.name = "idle-server"

include(":idle-api")
project(":idle-api").projectDir = file("idle-api")

include(":idle-domain")
project(":idle-domain").projectDir = file("idle-domain")

include(":idle-support:logging")
project(":idle-support:logging").projectDir = file("idle-support/logging")

include(":idle-support:jacoco")
project(":idle-support:jacoco").projectDir = file("idle-support/jacoco")

include(":idle-support:common")
project(":idle-support:common").projectDir = file("idle-support/common")

include(":idle-infrastructure:sms")
project(":idle-infrastructure:sms").projectDir = file("idle-infrastructure/sms")
