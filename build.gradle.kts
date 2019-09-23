import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11" apply false
    id("com.google.cloud.tools.jib") version "1.5.1" apply false
    id("org.springframework.boot") version "2.1.1.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.6.RELEASE" apply true
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.50" apply false
}

allprojects {
    group = "com.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("http://4thline.org/m2")
        maven("http://repo.spring.io/milestone")
    }
}

subprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    apply(plugin = "com.google.cloud.tools.jib")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Greenwich.RC2")
        }
    }
}