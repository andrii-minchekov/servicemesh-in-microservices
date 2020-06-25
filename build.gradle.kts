import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72" apply false
    id("com.google.cloud.tools.jib") version "1.5.1" apply false
    id("org.springframework.boot") version "2.2.3.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.6.RELEASE" apply true
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.72" apply false
}

allprojects {
    group = "com.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://repo.spring.io/milestone")
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
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR5")
            mavenBom("org.springframework.boot:spring-boot-dependencies:2.2.3.RELEASE")
//            {
//                bomProperty("jetty.version", "9.4.25.v20191220")
//            }
        }
    }
    configurations {
        all {
            //exclude(group="org.springframework.boot", module="spring-boot-starter-logging")
            exclude(group="org.slf4j", module="slf4j-log4j12")
        }
    }
}