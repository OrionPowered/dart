import com.github.quillmc.tinymcp.Version

plugins {
    java
    id("devtools")
}

dart {
    serverVersion.set(Version.BETA1_1__02)
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}