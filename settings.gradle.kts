plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "file-analysis-system"

include("api-gateway")
include("file-storing-service")
include("file-analysis-service")
