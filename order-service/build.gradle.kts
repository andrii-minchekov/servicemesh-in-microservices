import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.spring")
    id("com.gorylenko.gradle-git-properties") version ("2.4.1")
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

springBoot {
    buildInfo {
        properties {
            name = "Order Service Application"
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("org.hobsoft.spring:spring-rest-template-logger:2.0.0")
    implementation("io.micrometer:micrometer-registry-prometheus:1.1.2")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.7")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.7")

    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.2.0")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

val dockerRepository: String by project

jib {
    from {
        image = "gcr.io/distroless/java:debug"
    }
    to {
        image = "$dockerRepository/order-service"
        setCredHelper("docker-credential-osxkeychain")
    }
    val debugFlag = "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5556"
    container {
        ports = mutableListOf("8072")
        jvmFlags = mutableListOf("-Dhttp.proxyHost=linkerd", "-Dhttp.proxyPort=4141")
    }
}