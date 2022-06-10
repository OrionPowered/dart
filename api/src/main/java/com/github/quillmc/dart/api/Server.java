package com.github.quillmc.dart.api;

import com.alexsobiek.async.event.EventBus;
import com.github.quillmc.dart.api.chat.ChatFormatter;
import com.github.quillmc.dart.api.entity.Player;

import java.util.Optional;

public interface Server<P extends Player> {

    EventBus getEventBus();

    Optional<? extends P> getPlayer(String username);

    ChatFormatter<P> getChatFormatter();

    void setChatFormatter(ChatFormatter<P> formatter);

    void broadcast(String msg);

    void broadcastPlain(String msg);

    // Version Interfaces
    // Quick note: essentially objects such as Player will vary across different versions. We can number the versions
    // and plugins can then choose which version to use.

    /**
     * Base Server version supporting all Dart servers
     */
    interface All extends Server<Player> {
    }

    /**
     * V1 Dart Server. Same thing as {@link Server.All}
     */
    interface V1 extends All {
    }
}
