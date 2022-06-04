package net.tttproject.ariesbuildings.events.core;

import lombok.Getter;
import org.bukkit.entity.Player;

public class PlayerEventCore extends EventCore {

    @Getter private final Player player;

    public PlayerEventCore(Player player) {
        this.player = player;
    }

}
