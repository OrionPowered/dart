import com.github.quillmc.tinymcp.Version

plugins {
    java
    id("Quill.java-conventions")
    id("devtools")
}

dart {
    // This project is named b1.2_01 since the server b1.2_02 was never released for the server, only client.
    // For the sake of keeping things simple, TinyMCP only recognizes beta 1.2_02, but will use the b1.2_01 server.
    serverVersion.set(Version.BETA1_2__02)
}

dependencies {
    implementation(project(":dart"))
}