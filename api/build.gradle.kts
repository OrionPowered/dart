plugins {
    `java-library`
    id("Quill.java-conventions")
}

dependencies {
    api("ch.qos.logback:logback-classic:${project.property("logback_version")}")
    api("com.alexsobiek:async:${project.property("async_version")}")
    api("com.electronwill.night-config:toml:${project.property("nightconfig_version")}")
}