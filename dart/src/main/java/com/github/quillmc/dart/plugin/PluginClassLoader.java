package com.github.quillmc.dart.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class PluginClassLoader extends URLClassLoader {
    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    protected void loadClasses(JarFile jarFile) {
        jarFile.stream(). // Load all the classes in the plugin
                filter(e -> e.getName().endsWith(".class")).map(je -> je.getName().replace("/", ".").replace(".class", "")).toList().forEach(str -> {
            try {
                loadClass(str);
            } catch (ClassNotFoundException e) {
            }
        });
    }
}
