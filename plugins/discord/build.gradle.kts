plugins {
    `java-library`
    id("Quill.java-conventions-shadow")
}

dependencies {
    compileOnly(project(":api"))
    implementation("com.discord4j:discord4j-core:3.2.2")
}