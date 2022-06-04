package net.tttproject.ariesbuildings.events;

import lombok.Getter;
import net.tttproject.ariesbuildings.events.core.PlayerEventCore;
import org.bukkit.entity.Player;

public class PlayerAFKToggleEvent extends PlayerEventCore {

    @Getter private final AFKType afkType;

    public PlayerAFKToggleEvent(Player player, AFKType afkType) {
        super(player);
        this.afkType = afkType;
    }

    public enum AFKType {
        AFK,
        NO_LONGER_AFK
    }

}


