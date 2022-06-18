package com.github.quillmc.dart.api.event;

import com.alexsobiek.nexus.event.Event;
import com.github.quillmc.dart.api.entity.Player;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerChatEvent<P extends Player> implements Event, Cancellable {
    private final P player;
    private String message;

    public PlayerChatEvent(P player, String message) {
        this.player = player;
        this.message = message;
    }

    private boolean cancelled;
}
