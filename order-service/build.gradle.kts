import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun
import kotlin.collections.mutableListOf


plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.spring")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    "bootJar"(BootJar::class) {
        archiveName = "app.jar"
        mainClassName = "com.example.orderservice.OrderServiceApplication"
    }

    "bootRun"(BootRun::class) {
        main = "com.example.orderservice.OrderServiceApplication"
//        args("--spring.profiles.active=demo")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

jib {
    to {
        image = "menya84/order-service"
    }
    container {
        ports = mutableListOf("8072")
    }
}