import com.github.monosoul.yadegrap.DelombokTask

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow")
    id("com.github.monosoul.yadegrap")
}

group = "com.github.quill"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

dependencies{
    compileOnly("org.projectlombok:lombok:${project.property("lombok_version")}")
    annotationProcessor("org.projectlombok:lombok:${project.property("lombok_version")}")
}


tasks {
    val delombok = "delombok"(DelombokTask::class)

    "javadoc"(Javadoc::class) {
        dependsOn(delombok)
        setSource(delombok)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:unchecked")
}