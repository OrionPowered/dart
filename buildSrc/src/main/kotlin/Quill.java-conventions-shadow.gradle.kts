plugins {
    id("Quill.java-conventions")
    id("com.github.johnrengelman.shadow")
}

tasks {
    jar {
        archiveClassifier.set("unshaded")
    }

    shadowJar {
        minimize() {
            exclude(dependency("ch.qos.logback:logback-classic"))
            exclude(dependency("com.electronwill.night-config:toml"))
        }
        archiveClassifier.set("shaded")
    }

    build {
        dependsOn(shadowJar)
    }
}