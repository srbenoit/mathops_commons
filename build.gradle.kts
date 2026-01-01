plugins {
    `java-library`
    `maven-publish`
}

sourceSets {
    main {
        output.setResourcesDir(file("build/classes/java/main"))
    }
}

group = "com.github.srbenoit"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}