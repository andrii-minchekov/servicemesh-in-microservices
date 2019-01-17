import org.jetbrains.kotlin.com.intellij.openapi.vfs.StandardFileSystems.jar
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
        mainClassName = "com.example.orderservice.OrderServiceApplicationKt"
    }

    "bootRun"(BootRun::class) {
        main = "com.example.orderservice.OrderServiceApplicationKt"
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

val dockerRepository: String by project

jib {
    from {
        image = "gcr.io/distroless/java:debug"
    }
    to {
        image = "$dockerRepository/order-service"
    }
    val debugFlag = "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5556"
    container {
        ports = mutableListOf("8072")
        jvmFlags = mutableListOf("-Dhttp.proxyHost=linkerd", "-Dhttp.proxyPort=4141")
    }
}