package com.github.quillmc.dart;

import ch.qos.logback.classic.Level;
import com.alexsobiek.nexus.Nexus;
import com.alexsobiek.nexus.event.EventBus;
import com.alexsobiek.nexus.plugin.NexusPluginFramework;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.github.quillmc.dart.api.Server;
import com.github.quillmc.dart.api.chat.ChatFormatter;
import com.github.quillmc.dart.api.entity.Player;
import com.github.quillmc.dart.api.event.PlayerChatEvent;
import com.github.quillmc.dart.api.event.PlayerJoinEvent;
import com.github.quillmc.dart.api.event.PlayerLeaveEvent;
import com.github.quillmc.dart.api.util.ConfigUtil;
import com.github.quillmc.dart.plugin.DartPluginManager;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

@Setter
@Getter
public abstract class DartServer<P extends Player> implements Server<P> {
    protected static final Logger logger = LoggerFactory.getLogger("Dart");
    private final Nexus nexus = Nexus.builder().build();
    private final CommentedFileConfig config = ConfigUtil.getConfig(DartServer.class, new File("dart.toml"), "dart.toml");
    private NexusPluginFramework pluginFramework;
    private DartPluginManager pluginManager;
    private ChatFormatter<P> chatFormatter = (ChatFormatter<P>) ChatFormatter.DEFAULT;

    @SneakyThrows
    public void preStart() {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
        logger.info("Starting Dart");
        if (config == null) {
            logger.error("Failed getting dart.toml config");
            stop();
        }
        pluginManager = new DartPluginManager(this);
        pluginFramework = nexus.library(NexusPluginFramework.builder()
                .manager(pluginManager)
                .withDirectory(pluginManager.getPluginsDir().toPath())
                .build());
        logger.info("Loading plugins");
        pluginFramework.findAndLoadPlugins();
    }

    protected void onReady() {
        pluginManager.enableAll();
    }

    protected void onStop() {
        logger.info("Disabling plugins");
        pluginManager.disableAll();
    }

    @Override
    public EventBus getEventBus() {
        return nexus.eventBus();
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

    public boolean canUpdateBlock(Player player, int distanceFromSpawn) {
        int spawnProtection = config.getInt("spawn_protection");
        return spawnProtection == -1 || player.isOp() || distanceFromSpawn > spawnProtection;
    }

    protected abstract void stop();

    public abstract void broadcast(String msg);

    public abstract void broadcastPlain(String msg);
}
