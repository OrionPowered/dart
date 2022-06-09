package com.github.quillmc.dart.api;

import com.alexsobiek.async.event.Event;

import java.util.function.Consumer;

public abstract class DartPlugin {
    protected Server server;

    /**
     * Returns this server
     * @return Server
     */
    public Server getServer() {
        return server;
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
}
