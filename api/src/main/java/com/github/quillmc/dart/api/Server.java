package com.github.quillmc.dart.api;

import com.alexsobiek.async.event.EventBus;
import com.github.quillmc.dart.api.entity.Player;
import com.github.quillmc.dart.api.chat.ChatFormatter;

import java.util.Optional;

public interface Server<P extends Player> {
    EventBus getEventBus();
    Optional<? extends P> getPlayer(String username);

    ChatFormatter<P> getChatFormatter();

    void setChatFormatter(ChatFormatter<P> formatter);

    void broadcast(String msg);

    void broadcastPlain(String msg);
}
