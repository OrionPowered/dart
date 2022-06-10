package com.github.quillmc.dart;

import com.alexsobiek.async.AsyncUtility;
import com.alexsobiek.async.event.EventBus;
import com.github.quillmc.dart.api.Server;
import com.github.quillmc.dart.api.entity.Player;
import com.github.quillmc.dart.api.event.PlayerChatEvent;
import com.github.quillmc.dart.api.event.PlayerJoinEvent;
import com.github.quillmc.dart.api.event.PlayerLeaveEvent;
import com.github.quillmc.dart.api.chat.ChatFormatter;
import com.github.quillmc.dart.plugin.PluginLoader;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Setter
@Getter
public abstract class DartServer<P extends Player> implements Server<P> {
    protected static final Logger logger = LoggerFactory.getLogger("Dart");
    private final AsyncUtility async = AsyncUtility.builder().build();
    private ChatFormatter<P> chatFormatter = (ChatFormatter<P>) ChatFormatter.DEFAULT;
    private PluginLoader pluginLoader;

    @SneakyThrows
    public void preStart() {
        logger.info("Starting Dart");
        pluginLoader = new PluginLoader(this);
    }

    @Override
    public EventBus getEventBus() {
        return async.eventBus();
    }

    @SneakyThrows
    protected boolean handleJoin(P player) {
        PlayerJoinEvent<P> e = getEventBus().post(new PlayerJoinEvent<>(player)).get();
        return !e.isCancelled();
    }

    protected void handleLeave(P player) {
        getEventBus().post(new PlayerLeaveEvent<>(player));
    }

    @SneakyThrows
    protected Optional<String> handleChat(P player, String chat) {
        PlayerChatEvent<P> e = getEventBus().post(new PlayerChatEvent<>(player, chat)).get();
        if (e.isCancelled()) return Optional.empty();
        else return Optional.of(chatFormatter.format(player, chat));
    }

    public abstract void broadcast(String msg);

    public abstract void broadcastPlain(String msg);
}
