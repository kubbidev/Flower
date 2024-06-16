group = "net.flower"
version = "1.0"

tasks.jar {
    manifest {
        attributes(Pair("Automatic-Module-Name", "net.flower.api"))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "api"

            from(components["java"])
            pom {
                name = "Flower API"
                description = "A Minecraft plugin template that simplifies creating custom game modes and adventures with powerful tools for admins and developers."
                url = "https://kubbidev.com"

                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                    }
                }

                developers {
                    developer {
                        id = "kubbidev"
                        name = "kubbi"
                        url = "https://kubbidev.com"
                        email = "kubbidev@gmail.com"
                    }
                }

                issueManagement {
                    system = "Github"
                    url = "https://github.com/kubbidev/Flower/issues"
                }
            }
        }
    }
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
}