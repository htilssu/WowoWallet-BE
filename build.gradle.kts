plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
}
val springCloudVersion by extra("2023.0.2")


group = "com.ewallet"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")


    //jwt
    implementation("io.jsonwebtoken:jjwt:0.12.6")
    //one time password
    implementation("com.github.bastiaanjansen:otp-java:2.0.3") //twilio
    implementation("com.twilio.sdk:twilio:10.4.0") 
    
    //dynamodb
    implementation("software.amazon.awssdk:dynamodb-enhanced")

    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.google.guava:guava:33.2.1-jre")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    implementation ("com.google.code.gson:gson:2.11.0")



    //dynamodb
    implementation("software.amazon.awssdk:dynamodb-enhanced")
    runtimeOnly("org.postgresql:postgresql")



    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    "developmentOnly"("org.springframework.boot:spring-boot-devtools")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation ("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor ("org.mapstruct:mapstruct-processor:1.5.5.Final")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        mavenBom("software.amazon.awssdk:bom:2.26.12")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.compileJava { //encode UTF-8
    options.encoding = "UTF-8"
}
