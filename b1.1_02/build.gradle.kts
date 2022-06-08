import com.github.quillmc.tinymcp.Version

plugins {
    java
    id("devtools")
}

dart {
    serverVersion.set(Version.BETA1_1__02)
}