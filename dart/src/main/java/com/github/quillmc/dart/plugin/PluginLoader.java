package com.github.quillmc.dart.plugin;

import com.github.quillmc.dart.DartServer;
import com.github.quillmc.dart.api.exception.InvalidPluginException;
import com.github.quillmc.dart.api.plugin.DartPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;

public class PluginLoader extends PluginClassLoader {
    private final Logger logger = LoggerFactory.getLogger("PluginLoader");
    private final List<DartPlugin<?>> plugins = new ArrayList<>();
    private final File pluginFolder = new File("plugins");
    private final DartServer<?> server;

    protected Field serverF;
    protected Field loggerF;

    public PluginLoader(DartServer<?> server) {
        super(new URL[]{}, PluginLoader.class.getClassLoader());
        this.server = server;
        if (!pluginFolder.exists()) pluginFolder.mkdirs();

        try {
            serverF = DartPlugin.class.getDeclaredField("server");
            loggerF = DartPlugin.class.getDeclaredField("logger");
            if (!serverF.trySetAccessible()) throw new RuntimeException("Failed to set server field accessible of AbstractDartPlugin");
            if (!loggerF.trySetAccessible()) throw new RuntimeException("Failed to set logger field accessible of AbstractDartPlugin");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        loadJarPlugins();
    }

    private void loadJarPlugins() {
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

                    try (InputStream is = jarFile.getInputStream(jarFile.getEntry("plugin.properties"))) {
                        if (is == null) throw new InvalidPluginException("Missing plugin.properties");
                        Properties prop = new Properties();
                        prop.load(is);
                        loadPlugin((Class<? extends DartPlugin<?>>) Class.forName(prop.getProperty("main"),false, this));
                    } catch (IOException e) {
                        throw new InvalidPluginException("Failed to open plugin.properties", e);
                    }
                } catch (Throwable t) {
                    throw new RuntimeException(t); // TODO: handle
                }
            }
        }
    }

    public <P extends DartPlugin<?>> P loadPlugin(Class<? extends P> pluginClass) throws InvalidPluginException {
        try {
            P plugin = pluginClass.getConstructor().newInstance();
            logger.info("Enabling {} by {}", plugin.getName(), plugin.getAuthors());
            serverF.set(plugin, server);
            loggerF.set(plugin, LoggerFactory.getLogger(plugin.getName()));
            plugin.onEnable();
            plugins.add(plugin);
            logger.info("Enabled {}", plugin.getName());;
            return plugin;
        } catch (Throwable t) {
            throw new InvalidPluginException("Failed to load plugin from " + pluginClass, t);
        }
    }

}
