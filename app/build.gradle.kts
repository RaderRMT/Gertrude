plugins {
    java

    signing
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.5")
}

publishing {
    repositories {
        maven {
            name = "raderRepository"
            url = uri("https://repo.rader.fr/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "fr.rader"
            artifactId = "gertrude"
            version = "1.2.0"
            from(components["java"])
        }
    }
}