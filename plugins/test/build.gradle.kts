plugins {
    `java-library`
    id("Quill.java-conventions")
}

dependencies {
    compileOnly(project(":api"))
}