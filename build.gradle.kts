plugins {
    `java-library`
}

sourceSets {
    main {
        output.setResourcesDir(file("build/classes/java/main"))
    }
}

group = "dev.mathops.commons"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    compileOnly("jakarta.websocket:jakarta.websocket-api:2.2.0")
    compileOnly("jakarta.websocket:jakarta.websocket-client-api:2.2.0")

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileJava {
    options.javaModuleVersion = provider { "1.0" }
}