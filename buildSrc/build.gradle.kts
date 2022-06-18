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
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("gradle.plugin.com.github.monosoul:yadegrap:1.0.0")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.1.0.202203080745-r")
    implementation("com.github.QuillMC:TinyMCP:e46a0a9007")
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