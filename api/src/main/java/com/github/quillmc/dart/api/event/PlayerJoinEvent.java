package com.github.quillmc.dart.api.event;

import com.alexsobiek.async.event.Event;
import com.github.quillmc.dart.api.entity.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@RequiredArgsConstructor
public class PlayerJoinEvent<P extends Player> implements Event, Cancellable {
    private final P player;
    private boolean cancelled;
}
