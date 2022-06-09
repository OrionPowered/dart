plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.fabricmc.net")
    maven("https://jitpack.io")
}

dependencies {
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("gradle.plugin.com.github.monosoul:yadegrap:1.0.0")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.1.0.202203080745-r")
    implementation("com.github.QuillMC:TinyMCP:6b7af2d257")
    implementation("net.fabricmc:cfr:0.1.1")
}


gradlePlugin {
    plugins {
        create("devtools") {
            id = "devtools"
            implementationClass = "devtools.DartPlugin"
        }
    }
}