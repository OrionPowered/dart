package com.github.quillmc.dart.api.plugin;

import com.alexsobiek.nexus.event.Event;
import com.alexsobiek.nexus.inject.annotation.Inject;
import com.alexsobiek.nexus.plugin.NexusPlugin;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.github.quillmc.dart.api.Server;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public abstract class DartPlugin<S extends Server<?>> implements NexusPlugin {
    @Inject
    protected Server<?> server;
    @Inject
    protected Logger logger;
    @Inject(identifier = "dataFolder")
    protected File dataFolder;

    /**
     * Returns this server
     *
     * @return Server
     */
    public Server<?> getServer() {
        return server;
    }

    /**
     * Returns this plugin's logger
     *
     * @return Logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Returns this plugin's data folder
     *
     * @return File
     */
    public File getDataFolder() {
        return dataFolder;
    }

    /**
     * Creates a new CommentedFileConfig
     *
     * @param file File on disk to load
     * @param path Path of file in jar (if file does not exist)
     * @return CommentedFileConfig
     */
    public CommentedFileConfig getConfig(File file, String path) {
        try (InputStream is = getClass().getResourceAsStream((path.startsWith("/") ? path : "/" + path))) {
            CommentedFileConfig config = CommentedFileConfig.builder(file)
                    .onFileNotFound(FileNotFoundAction.copyData(is))
                    .autosave()
                    .build();
            config.load();
            is.close();
            return config;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Should never reach this point, an exception should be thrown
    }

    /**
     * Listen for an event - calls the consumer with the event object when fired.
     *
     * @param eventClass Class of event
     * @param listener   Consumer to consume event
     * @param <T>        Event type
     */
    public <T extends Event> void listen(Class<T> eventClass, Consumer<T> listener) {
        server.getEventBus().listen(eventClass, listener);
    }

    public abstract void onReady();
}
