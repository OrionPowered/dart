plugins {
    id("Quill.java-conventions")
    id("com.github.johnrengelman.shadow")
}

tasks {
    jar {
        archiveClassifier.set("unshaded")
    }

    shadowJar {
        archiveClassifier.set("shaded")
    }

    build {
        dependsOn(shadowJar)
    }
}