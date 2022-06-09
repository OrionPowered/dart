plugins {
    `java-library`
    id("Quill.java-conventions")
}

dependencies {
    implementation(project(":api"))
    implementation("com.alexsobiek:async:7cd100b5df")
    implementation("com.electronwill.night-config:toml:3.6.5")
}