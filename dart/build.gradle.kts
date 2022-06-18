plugins {
    `java-library`
    id("Quill.java-conventions-shadow")
}

dependencies {
    implementation(project(":api"))
    implementation("ch.qos.logback:logback-classic:${project.property("logback_version")}")
    implementation("com.alexsobiek.nexus:core:${project.property("nexus_version")}")
    implementation("com.alexsobiek.nexus:plugin:${project.property("nexus_version")}")
    implementation("com.electronwill.night-config:toml:${project.property("nightconfig_version")}")
}