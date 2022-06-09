package com.github.quillmc.dart.api.chat;

import com.github.quillmc.dart.api.entity.Player;

public interface ChatFormatter<P extends Player> {
    public static ChatFormatter<Player> DEFAULT = (player, message) -> String.format("<%s> %s", player.getUsername(), message);

    String format(P player, String message);
}
