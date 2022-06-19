package com.github.quillmc.dart.plugin.discord;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.github.quillmc.dart.api.Server;
import com.github.quillmc.dart.api.chat.ChatColor;
import com.github.quillmc.dart.api.entity.Player;
import com.github.quillmc.dart.api.event.PlayerChatEvent;
import com.github.quillmc.dart.api.event.PlayerJoinEvent;
import com.github.quillmc.dart.api.event.PlayerLeaveEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Webhook;
import discord4j.core.object.entity.channel.Channel;
import discord4j.discordjson.json.EmbedAuthorData;
import discord4j.discordjson.json.EmbedData;
import discord4j.discordjson.json.MessageCreateRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatService {
    private final CommentedFileConfig config;
    private final Server<?> server;
    private final Channel channel;
    private final Webhook webhook;

    public String avatarUrl(String username) {
        return ((String) config.get("chat.minecrafthead")).replace("{username}", username);
    }

    public MessageCreateRequest joinLeaveEmbed(String username, String action, int color) {
        return MessageCreateRequest.builder()
                .embed(EmbedData.builder()
                        .author(EmbedAuthorData.builder()
                                .name(String.format("%s %s the game", username, action))
                                .iconUrl(avatarUrl(username))
                                .build())
                        .color(color)
                        .build())
                .build();
    }

    public void sendDiscordMessage(String msg) {
        channel.getRestChannel().createMessage(msg).block();
    }

    public void onReady() {
        sendDiscordMessage(config.get("chat.startup"));
    }

    public void onShutdown() {
        sendDiscordMessage(config.get("chat.shutdown"));
    }

    public void onDiscordMessage(MessageCreateEvent event) {
        event.getMember().ifPresent(member -> {
            if (!member.isBot()) {
                server.broadcastPlain(ChatColor.translateColor('&', config.get("chat.minecraftformat"))
                        .replace("{username}", member.getUsername())
                        .replace("{message}", event.getMessage().getContent()));
            }
        });
    }

    public void onMinecraftChat(PlayerChatEvent<Player> event) {
        String username = event.getPlayer().getUsername();
        webhook.execute()
                .withUsername(username)
                .withAvatarUrl(avatarUrl(username))
                .withContent(event.getMessage())
                .block();
    }

    public void onPlayerJoin(PlayerJoinEvent<Player> event) {
        channel.getRestChannel().createMessage(joinLeaveEmbed(event.getPlayer().getUsername(), "joined", 0x00C853)).block();
    }

    public void onPlayerLeave(PlayerLeaveEvent<Player> event) {
        channel.getRestChannel().createMessage(joinLeaveEmbed(event.getPlayer().getUsername(), "left", 0xFF3D00)).block();
    }
}
