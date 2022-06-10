package com.github.quillmc.dart.testplugin;

import com.github.quillmc.dart.api.Server;
import com.github.quillmc.dart.api.event.PlayerChatEvent;
import com.github.quillmc.dart.api.event.PlayerJoinEvent;
import com.github.quillmc.dart.api.plugin.DartPlugin;

import java.util.List;

public class TestPlugin extends DartPlugin<Server.V1> {
    @Override
    public String getName() {
        return "TestPlugin";
    }

    @Override
    public List<String> getAuthors() {
        return List.of("QuillMC");
    }

    @Override
    public String getDescription() {
        return "Test Plugin for Dart";
    }

    @Override
    public void onEnable() {
        listen(PlayerChatEvent.class, e -> {
            getLogger().info("Chat event: {}", e.getMessage());
        });

        listen(PlayerJoinEvent.class, e -> {
            getLogger().info("Player join event: {}", e.getPlayer().getUsername());
        });
    }

    @Override
    public void onDisable() {
    }
}
