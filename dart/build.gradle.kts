plugins {
    `java-library`
    id("Quill.java-conventions")
}

dependencies {
    implementation(project(":api"))
    implementation("ch.qos.logback:logback-classic:${project.property("logback_version")}")
    implementation("com.alexsobiek:async:${project.property("async_version")}")
    implementation("com.electronwill.night-config:toml:${project.property("nightconfig_version")}")
}