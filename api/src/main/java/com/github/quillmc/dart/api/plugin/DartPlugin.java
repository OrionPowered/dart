package com.github.quillmc.dart.api.plugin;

import com.alexsobiek.nexus.event.Event;
import com.alexsobiek.nexus.inject.annotation.Inject;
import com.alexsobiek.nexus.lazy.Lazy;
import com.alexsobiek.nexus.plugin.NexusPlugin;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.github.quillmc.dart.api.Server;
import com.github.quillmc.dart.api.util.ConfigUtil;
import org.slf4j.Logger;

import java.io.File;
import java.util.Vector;
import java.util.function.Consumer;

public abstract class DartPlugin<S extends Server<?>> implements NexusPlugin {
    private final Vector<Runnable> reloadListeners = new Vector<>();

    @Inject
    protected Server<?> server;
    @Inject
    protected Logger logger;
    @Inject(identifier = "dataFolder")
    private Lazy<File> dataFolder;

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
        return dataFolder.get();
    }

    public void doReload() {
        reloadListeners.forEach(Runnable::run);
        onReload();
    }

    /**
     * Creates a new CommentedFileConfig
     *
     * @param file File on disk to load
     * @param path Path of file in jar (if file does not exist)
     * @return CommentedFileConfig
     */
    public CommentedFileConfig getConfig(File file, String path) {
        CommentedFileConfig config = ConfigUtil.getConfig(getClass(), file, path);
        System.out.println(config);
        reloadListeners.add(config::load);
        return config;
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
