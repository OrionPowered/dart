package com.github.quillmc.dart.plugin;

import com.github.quillmc.dart.DartServer;
import com.github.quillmc.dart.api.exception.InvalidPluginException;
import com.github.quillmc.dart.api.plugin.AbstractDartPlugin;
import com.github.quillmc.dart.api.plugin.PluginDescription;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class PluginLoader extends PluginClassLoader {
    private final List<AbstractDartPlugin<?>> plugins = new ArrayList<>();
    private final File pluginFolder = new File("plugins");
    private final DartServer<?> server;

    protected Field serverF;

    public PluginLoader(DartServer<?> server, URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.server = server;
        if (!pluginFolder.exists()) pluginFolder.mkdirs();

        try {
            serverF = AbstractDartPlugin.class.getDeclaredField("server");
            if (!serverF.trySetAccessible()) throw new RuntimeException("Failed to set server field accessible of AbstractDartPlugin");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadPlugins() {
        File[] pluginFiles = pluginFolder.listFiles();
        if (pluginFiles != null) {
            FilenameFilter filter = (dir, name) -> name.endsWith(".jar");
            List<File> jars = new ArrayList<>();
            for (File file : pluginFiles) {
                if (!file.isDirectory() && filter.accept(file.getParentFile(), file.getName())) {
                    jars.add(file);
                }
            }

            File[] jarFiles = jars.toArray(File[]::new);

            for (File file : jarFiles) {
                try {
                    addURL(file.toURI().toURL());
                    JarFile jarFile = new JarFile(file);
                    loadClasses(jarFile);

                    PluginDescription description = new PluginDescription(jarFile);

                    AbstractDartPlugin<?> plugin = (AbstractDartPlugin<?>) Class.forName(description.getMainClass(), false, this)
                            .getConstructors()[0].newInstance();

                    serverF.set(plugin, server);

                    plugins.add(plugin);
                    System.out.printf("Enabling %s by %s%n", description.getName(), description.getAuthor());
                    plugin.onEnable();
                    System.out.printf("%s enabled%n", description.getName());
                } catch (IOException | ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | InvalidPluginException e) {
                    throw new RuntimeException(e); // TODO: handle
                }
            }
        }
    }
}
