plugins {
    `java-library`
    id("Quill.java-conventions")
}

dependencies {
    api("ch.qos.logback:logback-classic:${project.property("logback_version")}")
    api("com.alexsobiek.nexus:core:${project.property("nexus_version")}")
    api("com.alexsobiek.nexus:plugin:${project.property("nexus_version")}")
    api("com.electronwill.night-config:toml:${project.property("nightconfig_version")}")
}