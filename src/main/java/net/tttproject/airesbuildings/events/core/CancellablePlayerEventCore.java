package net.tttproject.airesbuildings.events.core;

import lombok.Getter;
import org.bukkit.entity.Player;

public class CancellablePlayerEventCore extends CancellableEventCore {

    @Getter private final Player player;

    public CancellablePlayerEventCore(Player player, boolean cancelled) {
        super(cancelled);
        this.player = player;
    }
}
