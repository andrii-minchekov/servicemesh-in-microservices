group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.3.72"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

dependencies {
    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.2.0")
    testImplementation("io.rest-assured:rest-assured:4.3.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.9.7")
    testImplementation("org.assertj:assertj-core:3.16.1")
}