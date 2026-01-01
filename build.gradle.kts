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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.srbenoit"
            artifactId = "mathops-commons"
            version = "1.0.0"
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}