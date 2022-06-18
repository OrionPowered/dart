import com.github.quillmc.tinymcp.Version

plugins {
    application
    id("Quill.java-conventions-shadow")
    id("devtools")
}

application {
    mainClass.set("net.minecraft.server.MinecraftServer")
}

dart {
    serverVersion.set(Version.BETA1_1__02)
}

dependencies {
    implementation(project(":dart"))
    compileOnly(project(":api"))
    compileOnly("ch.qos.logback:logback-classic:${project.property("logback_version")}")
    compileOnly("com.alexsobiek.nexus:core:${project.property("nexus_version")}")
    compileOnly("com.electronwill.night-config:toml:${project.property("nightconfig_version")}")
}