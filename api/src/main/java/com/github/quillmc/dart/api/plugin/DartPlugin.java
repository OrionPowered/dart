package com.github.quillmc.dart.api.plugin;

import com.alexsobiek.async.event.Event;
import com.github.quillmc.dart.api.Server;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Consumer;

public abstract class DartPlugin<S extends Server<?>> {
    protected S server;
    protected Logger logger;

    /**
     * Returns this server
     * @return Server
     */
    public S getServer() {
        return server;
    }

    /**
     * Returns this plugin's logger
     * @return Logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Listen for an event - calls the consumer with the event object when fired.
     * @param eventClass Class of event
     * @param listener Consumer to consume event
     * @param <T> Event type
     */
    public <T extends Event> void listen(Class<T> eventClass, Consumer<T> listener) {
        server.getEventBus().listen(eventClass, listener);
    }

    public abstract String getName();

    public abstract List<String> getAuthors();

    public abstract String getDescription();

    public abstract void onEnable();

    public abstract void onDisable();
}
