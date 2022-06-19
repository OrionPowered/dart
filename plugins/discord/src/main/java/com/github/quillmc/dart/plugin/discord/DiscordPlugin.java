package com.github.quillmc.dart.plugin.discord;

import com.alexsobiek.nexus.plugin.annotation.Plugin;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.github.quillmc.dart.api.Server;
import com.github.quillmc.dart.api.event.PlayerChatEvent;
import com.github.quillmc.dart.api.event.PlayerJoinEvent;
import com.github.quillmc.dart.api.event.PlayerLeaveEvent;
import com.github.quillmc.dart.api.plugin.DartPlugin;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.DisconnectEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Webhook;
import discord4j.discordjson.json.WebhookCreateRequest;
import discord4j.discordjson.json.WebhookData;
import discord4j.rest.service.WebhookService;
import reactor.core.publisher.Mono;

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
    private ChatService chatService;
    private long channelId;

    @Override
    public void onReady() {
        config = getConfig(new File(getDataFolder(), "config.toml"), "config.toml");
        channelId = Long.parseLong(config.get("discord.channel"));
        logger.info("Logging into Discord...");
        new Thread(this::login).start();
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onDisable() {
        logger.info("Shutting down Discord bot");
        gateway.logout().block();
    }

    private void login() {
        DiscordClient.create(config.get("discord.token")).withGateway(this::handleGateway).block();
    }

    private Mono<Void> handleGateway(GatewayDiscordClient client) {
        this.gateway = client;

        // Once we've logged in and have a gateway client, we can start the chat service
        chatService = new ChatService(config, getServer(), client.getChannelById(Snowflake.of(channelId)).block(), getWebhook(client));
        listen(PlayerChatEvent.class, chatService::onMinecraftChat);
        listen(PlayerJoinEvent.class, chatService::onPlayerJoin);
        listen(PlayerLeaveEvent.class, chatService::onPlayerLeave);

        client.getEventDispatcher().on(ReadyEvent.class).subscribe(this::onDiscordReady);
        client.getEventDispatcher().on(DisconnectEvent.class).subscribe(e -> chatService.onShutdown());
        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(chatService::onDiscordMessage);
        return client.onDisconnect();
    }

    private Webhook getWebhook(GatewayDiscordClient client) {
        WebhookService webhookService = gateway.getRestClient().getWebhookService();
        WebhookData webhookData;
        webhookData = webhookService.getChannelWebhooks(channelId).filter(w -> w.name().isPresent() && w.name().equals("DartDiscord")).blockFirst();
        if (webhookData == null) // webhook doesn't exist, create one
            webhookData = webhookService.createWebhook(channelId, WebhookCreateRequest.builder().name("DartDiscord").build(), "Dart Discord Plugin").block();
        return client.getWebhookById(Snowflake.of(webhookData.id())).block();
    }

    public void onDiscordReady(ReadyEvent event) {
        logger.info("Logged in as {}", event.getSelf().getUsername());
        chatService.onReady();
    }
}
