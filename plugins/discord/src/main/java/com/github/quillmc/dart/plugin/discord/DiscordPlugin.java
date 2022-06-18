package com.github.quillmc.dart.plugin.discord;

import com.alexsobiek.nexus.plugin.annotation.Plugin;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.github.quillmc.dart.api.Server;
import com.github.quillmc.dart.api.chat.ChatColor;
import com.github.quillmc.dart.api.entity.Player;
import com.github.quillmc.dart.api.event.PlayerChatEvent;
import com.github.quillmc.dart.api.plugin.DartPlugin;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.io.File;

@Plugin(
        name = "Discord Plugin",
        description = "Discord chat bridge plugin for Dart",
        version = "1.0",
        authors = {"Dart Developers"}
)
public class DiscordPlugin extends DartPlugin<Server.All> {
    private CommentedFileConfig config;
    private GatewayDiscordClient gateway;

    @Override
    public void onReady() {
        config = getConfig(new File(getDataFolder(), "config.toml"), "config.toml");
        logger.info("Logging into Discord...");
        new Thread(this::login).start();
    }

    @Override
    public void onEnable() {
        listen(PlayerChatEvent.class, this::onMinecraftChat);
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onDisable() {

    }

    private void login() {
        DiscordClient.create(config.get("discord.token"))
                .withGateway(client -> {
                    this.gateway = client;
                    client.getEventDispatcher().on(ReadyEvent.class).subscribe(this::onDiscordReady);
                    client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(this::onDiscordMessage);
                    return client.onDisconnect();
                }).block();
    }

    public void onDiscordReady(ReadyEvent event) {
        logger.info("Logged in as {}", event.getSelf().getUsername());
    }

    public void onDiscordMessage(MessageCreateEvent event) {
        event.getMember().ifPresent(member -> {
            if (!member.isBot()) {
                getServer().broadcastPlain(ChatColor.translateColor('&', config.get("chat.minecraftformat"))
                        .replace("{username}", member.getUsername())
                        .replace("{message}", event.getMessage().getContent()));
            }
        });
    }

    public void onMinecraftChat(PlayerChatEvent<Player> event) {

    }
}
