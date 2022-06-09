package com.github.quillmc.dart.api.plugin;

import com.alexsobiek.async.event.Event;
import com.github.quillmc.dart.api.Server;
import com.github.quillmc.dart.api.entity.Player;

import java.util.function.Consumer;

public abstract class AbstractDartPlugin<P extends Player> {
    protected Server<P> server;

    /**
     * Returns this server
     * @return Server
     */
    public Server<P> getServer() {
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

    public abstract void onEnable();

    public abstract void onDisable();
}
