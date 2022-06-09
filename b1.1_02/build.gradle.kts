import com.github.quillmc.tinymcp.Version

plugins {
    java
    id("Quill.java-conventions")
    id("devtools")
}

dart {
    serverVersion.set(Version.BETA1_1__02)
}

dependencies {
    implementation(project(":dart"))
}