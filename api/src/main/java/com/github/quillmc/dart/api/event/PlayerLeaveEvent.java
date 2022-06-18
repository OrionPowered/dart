package com.github.quillmc.dart.api.event;

import com.alexsobiek.nexus.event.Event;
import com.github.quillmc.dart.api.entity.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlayerLeaveEvent<P extends Player> implements Event {
    private final P player;
}
