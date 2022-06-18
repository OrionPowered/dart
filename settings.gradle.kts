rootProject.name = "Dart"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://jitpack.io")
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}


include("dart", "api", "b1.1_02", "b1.2_01");

plugin("test");

fun plugin(name: String) {
    setupSubProject("plugin-$name") {
        projectDir = file("plugins/$name");
    }
}

inline fun setupSubProject(name: String, block: ProjectDescriptor.() -> Unit) {
    include(name);
    project(":$name").apply(block);
}